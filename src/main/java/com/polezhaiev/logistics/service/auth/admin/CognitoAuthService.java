package com.polezhaiev.logistics.service.auth.admin;

import com.polezhaiev.logistics.config.CognitoProperties;
import com.polezhaiev.logistics.dto.admin.*;
import com.polezhaiev.logistics.mapper.AdminMapper;
import com.polezhaiev.logistics.model.Admin;
import com.polezhaiev.logistics.repository.admin.AdminRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Service
@RequiredArgsConstructor
public class CognitoAuthService implements AuthService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final CognitoProperties cognitoProperties;

    private final Dotenv dotenv = Dotenv.load();

    private final AwsBasicCredentials credentials = AwsBasicCredentials.create(
            dotenv.get("AWS_ACCESS_KEY_ID"),
            dotenv.get("AWS_SECRET_ACCESS_KEY")
    );

    private CognitoIdentityProviderClient cognitoClient;

    @PostConstruct
    private void initCognitoClient() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(dotenv.get("COGNITO_REGION")))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
    @Override
    @Transactional
    public AdminResponseDto registerAdmin(AdminRequestDto adminRequestDto) {
        if (adminRepository.findByEmail(adminRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Admin with this email already exists.");
        }

        // ✅ 1. Создаем пользователя в AWS Cognito
        AdminCreateUserRequest cognitoRequest = AdminCreateUserRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(adminRequestDto.getEmail())
                .temporaryPassword(adminRequestDto.getPassword()) // Временный пароль
                .userAttributes(
                        AttributeType.builder().name("email").value(adminRequestDto.getEmail()).build(),
                        AttributeType.builder().name("name").value(adminRequestDto.getName()).build()
                )
                .messageAction(MessageActionType.SUPPRESS) // Отключаем email-уведомление
                .build();

        cognitoClient.adminCreateUser(cognitoRequest);

        // 2. Устанавливаем пароль как постоянный, чтобы не требовалась его смена
        AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(adminRequestDto.getEmail())
                .password(adminRequestDto.getPassword())
                .permanent(true)
                .build();

        cognitoClient.adminSetUserPassword(setPasswordRequest);

        // ✅ 2. Добавляем пользователя в группу "ADMIN"
        AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(adminRequestDto.getEmail())
                .groupName("ADMIN")
                .build();

        cognitoClient.adminAddUserToGroup(groupRequest);

        // ✅ 3. Сохраняем админа в базе данных
        Admin admin = adminMapper.toModel(adminRequestDto);
        adminRepository.save(admin);

        return adminMapper.toDto(admin);
    }

    @Override
    public AdminLoginResponseDto authenticateAdmin(AdminLoginRequestDto loginRequestDto) {
        // ✅ Проверяем, есть ли админ в БД
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(loginRequestDto.getEmail());
        if (optionalAdmin.isEmpty()) {
            throw new RuntimeException("Admin not found.");
        }

        // ✅ Отправляем запрос на Cognito для получения токена
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", loginRequestDto.getEmail());
        authParams.put("PASSWORD", loginRequestDto.getPassword());

        String secretHash = calculateSecretHash(
                loginRequestDto.getEmail(),
                cognitoProperties.getClientId(),
                cognitoProperties.getClientSecret()
        );
        authParams.put("SECRET_HASH", secretHash);

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .clientId(cognitoProperties.getClientId())
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(authParams)
                .build();

        AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);

        return new AdminLoginResponseDto(authResponse.authenticationResult().accessToken());
    }

    private String calculateSecretHash(String username, String clientId, String clientSecret) {
        try {
            String data = username + clientId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec signingKey = new SecretKeySpec(clientSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating secret hash", e);
        }
    }
}

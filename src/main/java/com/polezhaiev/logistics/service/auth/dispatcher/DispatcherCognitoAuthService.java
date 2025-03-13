package com.polezhaiev.logistics.service.auth.dispatcher;

import com.polezhaiev.logistics.config.CognitoProperties;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginResponseDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherResponseDto;
import com.polezhaiev.logistics.mapper.DispatcherMapper;
import com.polezhaiev.logistics.model.Dispatcher;
import com.polezhaiev.logistics.repository.DispatcherRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MessageActionType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DispatcherCognitoAuthService implements DispatcherAuthService {
    private final DispatcherRepository dispatcherRepository;
    private final DispatcherMapper dispatcherMapper;
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
    public DispatcherResponseDto registerDispatcher(DispatcherRequestDto dispatcherRequestDto) {
        if (dispatcherRepository.findByEmail(dispatcherRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Dispatcher with this email already exists.");
        }

        // ✅ 1. Создаем пользователя в AWS Cognito
        AdminCreateUserRequest cognitoRequest = AdminCreateUserRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(dispatcherRequestDto.getEmail())
                .temporaryPassword(dispatcherRequestDto.getPassword()) // Временный пароль
                .userAttributes(
                        AttributeType.builder().name("email").value(dispatcherRequestDto.getEmail()).build(),
                        AttributeType.builder().name("name").value(dispatcherRequestDto.getName()).build()
                )
                .messageAction(MessageActionType.SUPPRESS) // Отключаем email-уведомление
                .build();

        cognitoClient.adminCreateUser(cognitoRequest);

        // 2. Устанавливаем пароль как постоянный, чтобы не требовалась его смена
        AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(dispatcherRequestDto.getEmail())
                .password(dispatcherRequestDto.getPassword())
                .permanent(true)
                .build();

        cognitoClient.adminSetUserPassword(setPasswordRequest);

        // ✅ 2. Добавляем пользователя в группу "DISPATCHER"
        AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(dispatcherRequestDto.getEmail())
                .groupName("DISPATCHER")
                .build();

        cognitoClient.adminAddUserToGroup(groupRequest);

        // ✅ 3. Сохраняем диспетчера в базе данных
        Dispatcher dispatcher = dispatcherMapper.toModel(dispatcherRequestDto);
        dispatcherRepository.save(dispatcher);

        return dispatcherMapper.toDto(dispatcher);
    }

    @Override
    public DispatcherLoginResponseDto authenticateDispatcher(DispatcherLoginRequestDto loginRequestDto) {
        // ✅ Проверяем, есть ли диспетчер в БД
        Optional<Dispatcher> optionalDispatcher = dispatcherRepository.findByEmail(loginRequestDto.getEmail());
        if (optionalDispatcher.isEmpty()) {
            throw new RuntimeException("Dispatcher not found.");
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

        return new DispatcherLoginResponseDto(authResponse.authenticationResult().accessToken());
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

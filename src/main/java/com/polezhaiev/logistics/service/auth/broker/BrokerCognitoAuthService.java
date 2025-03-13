package com.polezhaiev.logistics.service.auth.broker;

import com.polezhaiev.logistics.config.CognitoProperties;
import com.polezhaiev.logistics.dto.broker.BrokerLoginRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerLoginResponseDto;
import com.polezhaiev.logistics.dto.broker.BrokerRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerResponseDto;
import com.polezhaiev.logistics.mapper.BrokerMapper;
import com.polezhaiev.logistics.model.Broker;
import com.polezhaiev.logistics.repository.BrokerRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MessageActionType;

@Service
@RequiredArgsConstructor
public class BrokerCognitoAuthService implements BrokerAuthService{
    private final BrokerRepository brokerRepository;
    private final BrokerMapper brokerMapper;
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
    public BrokerResponseDto registerBroker(BrokerRequestDto brokerRequestDto) {
        if (brokerRepository.findByEmail(brokerRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Broker with this email already exists.");
        }

        // ✅ 1. Создаем пользователя в AWS Cognito
        AdminCreateUserRequest cognitoRequest = AdminCreateUserRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(brokerRequestDto.getEmail())
                .temporaryPassword(brokerRequestDto.getPassword()) // Временный пароль
                .userAttributes(
                        AttributeType.builder().name("email").value(brokerRequestDto.getEmail()).build(),
                        AttributeType.builder().name("name").value(brokerRequestDto.getCompany()).build()
                )
                .messageAction(MessageActionType.SUPPRESS) // Отключаем email-уведомление
                .build();

        cognitoClient.adminCreateUser(cognitoRequest);

        // 2. Устанавливаем пароль как постоянный, чтобы не требовалась его смена
        AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(brokerRequestDto.getEmail())
                .password(brokerRequestDto.getPassword())
                .permanent(true)
                .build();

        cognitoClient.adminSetUserPassword(setPasswordRequest);

        // ✅ 2. Добавляем пользователя в группу "BROKER"
        AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(brokerRequestDto.getEmail())
                .groupName("BROKER")
                .build();

        cognitoClient.adminAddUserToGroup(groupRequest);

        AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(brokerRequestDto.getEmail())
                .build();

        AdminGetUserResponse getUserResponse = cognitoClient.adminGetUser(getUserRequest);

        String cognitoSub = getUserResponse.userAttributes().stream()
                .filter(attr -> attr.name().equals("sub"))
                .findFirst()
                .map(AttributeType::value)
                .orElseThrow(() -> new RuntimeException("sub not found for user"));

// Сохраняем брокера в БД
        Broker broker = brokerMapper.toModel(brokerRequestDto);
        broker.setCognitoSub(cognitoSub); // Сохраняем sub
        brokerRepository.save(broker);

        return brokerMapper.toDto(broker);
    }

    @Override
    public BrokerLoginResponseDto authenticateBroker(BrokerLoginRequestDto loginRequestDto) {
        // ✅ Проверяем, есть ли брокер в БД
        Optional<Broker> optionalBroker = brokerRepository.findByEmail(loginRequestDto.getEmail());
        if (optionalBroker.isEmpty()) {
            throw new RuntimeException("Broker not found.");
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

        return new BrokerLoginResponseDto(authResponse.authenticationResult().accessToken());
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

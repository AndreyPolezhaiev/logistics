package com.polezhaiev.logistics.service.auth.driver;

import com.polezhaiev.logistics.config.CognitoProperties;
import com.polezhaiev.logistics.dto.driver.DriverLoginRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverLoginResponseDto;
import com.polezhaiev.logistics.dto.driver.DriverRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverResponseDto;
import com.polezhaiev.logistics.mapper.DriverMapper;
import com.polezhaiev.logistics.model.Driver;
import com.polezhaiev.logistics.repository.DriverRepository;
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

@Service
@RequiredArgsConstructor
public class DriverCognitoAuthService implements DriverAuthService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
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
    public DriverResponseDto registerDriver(DriverRequestDto driverRequestDto) {
        if (driverRepository.findByEmail(driverRequestDto.getEmail()).isPresent()) {
            throw new RuntimeException("Driver with this email already exists.");
        }

        AdminCreateUserRequest cognitoRequest = AdminCreateUserRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(driverRequestDto.getEmail())
                .temporaryPassword(driverRequestDto.getPassword())
                .userAttributes(
                        AttributeType.builder().name("email").value(driverRequestDto.getEmail()).build(),
                        AttributeType.builder().name("name").value(driverRequestDto.getName()).build()
                )
                .messageAction(MessageActionType.SUPPRESS)
                .build();

        cognitoClient.adminCreateUser(cognitoRequest);

        AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(driverRequestDto.getEmail())
                .password(driverRequestDto.getPassword())
                .permanent(true)
                .build();

        cognitoClient.adminSetUserPassword(setPasswordRequest);

        AdminAddUserToGroupRequest groupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(cognitoProperties.getUserPoolId())
                .username(driverRequestDto.getEmail())
                .groupName("DRIVER")
                .build();

        cognitoClient.adminAddUserToGroup(groupRequest);

        Driver driver = driverMapper.toModel(driverRequestDto);
        driverRepository.save(driver);

        return driverMapper.toDto(driver);
    }

    @Override
    public DriverLoginResponseDto authenticateDriver(DriverLoginRequestDto loginRequestDto) {
        Optional<Driver> driver = driverRepository.findByEmail(loginRequestDto.getEmail());
        if (driver.isEmpty()) {
            throw new RuntimeException("Driver not found.");
        }

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

        return new DriverLoginResponseDto(authResponse.authenticationResult().accessToken());
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

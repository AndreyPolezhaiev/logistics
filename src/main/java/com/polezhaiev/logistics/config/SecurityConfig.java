package com.polezhaiev.logistics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.provider.cognito.issuerUri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Диспетчеры и водители могут только читать
                        .requestMatchers(HttpMethod.GET, "/api/dispatcher/**").hasAnyAuthority("DISPATCHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/driver/**").hasAnyAuthority("DISPATCHER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/broker/**").hasAnyAuthority("DISPATCHER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login-admin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-dispatcher").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login-dispatcher").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-driver").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login-driver").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register-broker").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/auth/login-broker").permitAll()
                        // Только админ может создавать, обновлять и удалять
                        .requestMatchers(HttpMethod.POST, "/api/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("ADMIN")

                        // Остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}

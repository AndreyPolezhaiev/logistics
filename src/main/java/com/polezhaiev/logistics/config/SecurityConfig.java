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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.client.provider.cognito.issuerUri}")
    private String issuerUri;

    private static final String[] ADMIN_PATHS = {
            "/api/dispatcher", "/api/dispatcher/**",
            "/api/driver", "/api/driver/**",
            "/api/broker", "/api/broker/**"
    };

    private static final String[] FREIGHT_PATHS = {
            "/api/freight", "/api/freight/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/register-admin",
                                "/api/auth/login-admin",
                                "/api/auth/login-dispatcher",
                                "/api/auth/login-driver",
                                "/api/auth/login-broker"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/register-dispatcher",
                                "/api/auth/register-driver",
                                "/api/auth/register-broker"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/dispatcher/**",
                                "/api/driver/**",
                                "/api/broker/**"
                        ).hasAnyAuthority("DISPATCHER", "ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/driver/location/**"
                        ).hasAnyAuthority("DISPATCHER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, ADMIN_PATHS).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PUT, ADMIN_PATHS).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, ADMIN_PATHS).hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.POST, FREIGHT_PATHS).hasAnyAuthority("ADMIN", "BROKER")
                        .requestMatchers(HttpMethod.PUT, FREIGHT_PATHS).hasAnyAuthority("ADMIN", "BROKER")
                        .requestMatchers(HttpMethod.DELETE, FREIGHT_PATHS).hasAnyAuthority("ADMIN", "BROKER")
                        .requestMatchers(HttpMethod.GET, FREIGHT_PATHS).hasAnyAuthority("ADMIN", "BROKER")

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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

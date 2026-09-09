package com.industrial.gateway.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        @Value("${security.jwt.secret}")
        private String jwtSecret;

        @Value("${security.cors.allowed-origins}")
        private String allowedOrigins;

        @Bean
        SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

                authoritiesConverter.setAuthorityPrefix("ROLE_");
                authoritiesConverter.setAuthoritiesClaimName("roles");

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                                authoritiesConverter);

                return http
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                .authorizeExchange(exchanges -> exchanges

                                                // Browser CORS preflight must never require JWT
                                                .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**")
                                                .permitAll()

                                                // Public endpoints
                                                .pathMatchers(
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/refresh",
                                                                "/actuator/health",
                                                                "/actuator/prometheus")
                                                .permitAll()

                                                // Everything else requires authentication
                                                .anyExchange()
                                                .authenticated())

                                .oauth2ResourceServer(oauth -> oauth
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                                new ReactiveJwtAuthenticationConverterAdapter(
                                                                                jwtAuthenticationConverter))))

                                .build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {

                CorsConfiguration config = new CorsConfiguration();

                List<String> origins = Arrays.stream(allowedOrigins.split(","))
                                .map(String::trim)
                                .filter(origin -> !origin.isBlank())
                                .toList();

                config.setAllowedOrigins(origins);

                config.setAllowedMethods(List.of(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"));

                config.setAllowedHeaders(List.of("*"));

                config.setExposedHeaders(List.of(
                                "X-Correlation-Id"));

                config.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", config);

                return source;
        }

        @Bean
        ReactiveJwtDecoder jwtDecoder() {

                SecretKey key = new SecretKeySpec(
                                jwtSecret.getBytes(StandardCharsets.UTF_8),
                                "HmacSHA256");

                return NimbusReactiveJwtDecoder
                                .withSecretKey(key)
                                .macAlgorithm(MacAlgorithm.HS256)
                                .build();
        }
}
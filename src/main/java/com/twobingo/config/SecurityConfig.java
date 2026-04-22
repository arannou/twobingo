package com.twobingo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Optional;
import com.twobingo.model.User;
import com.twobingo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(
                Arrays.asList("http://localhost:5173", "http://localhost", "http://localhost:80"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/actuator/**", configuration);
        return source;
    }

    /**
     * Custom AuthorizationManager that validates tokens from Authorization header.
     * Supports Bearer tokens: Authorization: Bearer <token>
     */
    @Bean
    public AuthorizationManager<RequestAuthorizationContext> tokenAuthorizationManager() {
        return (authentication, context) -> {
            String authHeader = context.getRequest().getHeader("Authorization");

            // Check if Authorization header exists and starts with "Bearer "
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Remove "Bearer " prefix
                Optional<User> userOpt = userRepository.findByConnectionToken(token);

                if (userOpt.isPresent() && token.equals(userOpt.get().getConnectionToken())) {
                    System.out.println("========= \n Auth is granted for route: " + context.getRequest().getRequestURI()
                            + " =========");
                    return new AuthorizationDecision(true);
                }
            }

            return new AuthorizationDecision(false);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/api/twobingo/health").permitAll()
                        .requestMatchers("/api/twobingo/users/login").permitAll()
                        .requestMatchers("/api/twobingo/users/register").permitAll()
                        .requestMatchers("/actuator/health").permitAll()

                        // Protected endpoints - require valid bearer token
                        .requestMatchers("/api/twobingo/**").access(tokenAuthorizationManager())
                        .requestMatchers("/actuator/**").access(tokenAuthorizationManager())

                        // Deny everything else by default
                        .anyRequest().denyAll())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }

}

package com.syed.assignment.congestion_tax_calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for managing security settings.
 *
 * this class is kept simple as no authentication is required for this API
 * CSRF protection is disabled because the API doesn't require session-based security
 * it can be enabled with enhancements on this API
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disabled CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());  // Allow all requests for now
        return http.build();
    }
}
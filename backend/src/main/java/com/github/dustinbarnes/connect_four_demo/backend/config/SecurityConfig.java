package com.github.dustinbarnes.connect_four_demo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final TokenAuthFilter tokenAuthFilter;

    public SecurityConfig(TokenAuthFilter jwtAuthFilter) {
        this.tokenAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/auth/is-logged-in").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling((exh) -> {
                // By default, Spring Security will send a 403 Forbidden response
                // for unauthorized access. I much prefer the 401 here. 
                exh.authenticationEntryPoint((_, response, _) -> {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                });
            });
        return http.build();
    }
}

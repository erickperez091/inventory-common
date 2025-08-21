package com.example.common.configuration.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

@Configuration
@ConditionalOnProperty(name = "security.enabled", havingValue = "false")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class NoSecurityConfig {

    private final static Logger LOGGER = Logger.getLogger(NoSecurityConfig.class.getName());

    @Bean
    public SecurityFilterChain noSecurity(HttpSecurity http) throws Exception {
        LOGGER.info("[SecurityConfig]:[noSecurity]:[Info]: No security enabled");
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityContext(sc -> sc.requireExplicitSave(false))
                .exceptionHandling(AbstractHttpConfigurer::disable);

        return http.build();
    }
}

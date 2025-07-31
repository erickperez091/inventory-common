package com.example.common.configuration;

import com.example.common.utilities.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private long expirationMilis;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secret, expirationMilis);
    }
}

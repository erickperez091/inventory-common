package com.example.common.security;// src/test/java/com/example/common/security/GlobalSecurityConfigTest.java
import com.example.common.configuration.security.CustomAuthEntryPoint;
import com.example.common.configuration.security.GlobalSecurityConfig;
import com.example.common.service.cache.CacheService;
import com.example.common.utilities.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GlobalSecurityConfigTest {

    private JwtUtils jwtUtils;
    private CacheService cacheService;
    private CustomAuthEntryPoint customAuthEntryPoint;
    private GlobalSecurityConfig config;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        cacheService = mock(CacheService.class);
        customAuthEntryPoint = mock(CustomAuthEntryPoint.class);
        config = new GlobalSecurityConfig(jwtUtils, cacheService, customAuthEntryPoint);
    }

    @Test
    void filterChain_createsSecurityFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        // Do not stub http.build()
        assertDoesNotThrow(() -> config.filterChain(http));
    }
    @Test
    void jwtAuthenticationFilter_createsFilter() {
        assertNotNull(config.jwtAuthenticationFilter());
    }

    @Test
    void authenticationManager_returnsManager() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);
        when(configuration.getAuthenticationManager()).thenReturn(manager);
        assertEquals(manager, config.authenticationManager(configuration));
    }
}
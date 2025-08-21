package com.example.common.security;

import com.example.common.configuration.security.NoSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NoSecurityConfigTest {

    @Test
    void noSecurity_configuresHttpSecurityAndReturnsFilterChain() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);
        DefaultSecurityFilterChain filterChain = mock(DefaultSecurityFilterChain.class);

        when(http.securityMatcher(anyString())).thenReturn(http);
        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.securityContext(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);
        when(http.build()).thenReturn(filterChain);

        NoSecurityConfig config = new NoSecurityConfig();
        var result = config.noSecurity(http);

        assertEquals(filterChain, result);
        verify(http).securityMatcher("/**");
        verify(http).csrf(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).sessionManagement(any());
        verify(http).securityContext(any());
        verify(http).exceptionHandling(any());
        verify(http).build();
    }
}

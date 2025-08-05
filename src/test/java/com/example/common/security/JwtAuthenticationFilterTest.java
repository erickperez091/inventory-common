package com.example.common.security;


import com.example.common.BlackListAuthException;
import com.example.common.security.CustomAuthEntryPoint;
import com.example.common.security.JwtAuthenticationFilter;
import com.example.common.service.CacheService;
import com.example.common.utilities.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


class JwtAuthenticationFilterTest {

    private JwtUtils jwtUtils;
    private CacheService cacheService;
    private CustomAuthEntryPoint customAuthEntryPoint;
    private JwtAuthenticationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        cacheService = mock(CacheService.class);
        customAuthEntryPoint = mock(CustomAuthEntryPoint.class);
        filter = new JwtAuthenticationFilter(jwtUtils, cacheService, customAuthEntryPoint);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_validToken_setsAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken");
        when(jwtUtils.getUsername("validtoken")).thenReturn("user");
        when(jwtUtils.isTokenValid("validtoken")).thenReturn(true);
        when(cacheService.isTokenInBlackList("validtoken")).thenReturn(false);
        when(jwtUtils.getRole("validtoken")).thenReturn("USER");

        filter.doFilter(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("user", auth.getPrincipal());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customAuthEntryPoint);
    }

    @Test
    void doFilterInternal_blacklistedToken_triggersEntryPoint() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer blacklistedtoken");
        when(jwtUtils.getUsername("blacklistedtoken")).thenReturn("user");
        when(jwtUtils.isTokenValid("blacklistedtoken")).thenReturn(true);
        when(cacheService.isTokenInBlackList("blacklistedtoken")).thenReturn(true);

        filter.doFilter(request, response, filterChain);

        verify(customAuthEntryPoint).commence(eq(request), eq(response), any(BlackListAuthException.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_noToken_callsNextFilter() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customAuthEntryPoint);
    }

    @Test
    void doFilterInternal_invalidToken_callsNextFilter() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidtoken");
        when(jwtUtils.isTokenValid("invalidtoken")).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(customAuthEntryPoint);
    }
}
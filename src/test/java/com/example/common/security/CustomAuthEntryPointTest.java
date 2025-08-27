package com.example.common.security;

import com.example.common.BlackListAuthException;
import com.example.common.configuration.security.CustomAuthEntryPoint;
import com.example.common.entity.dto.AuthErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomAuthEntryPointTest {

    private CustomAuthEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        entryPoint = new CustomAuthEntryPoint();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void commence_withInsufficientAuthenticationException_returnsCorrectError() throws Exception {
        AuthenticationException ex = new InsufficientAuthenticationException("Not enough auth");
        entryPoint.commence(request, response, ex);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        AuthErrorResponse error = new ObjectMapper().readValue(captor.getValue(), AuthErrorResponse.class);
        assertEquals("AUTH-401-INSUFFICIENT-AUTH", error.error());
    }

    @Test
    void commence_withBlackListAuthException_returnsCorrectError() throws Exception {
        AuthenticationException ex = new BlackListAuthException("Token expired");
        entryPoint.commence(request, response, ex);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        AuthErrorResponse error = new ObjectMapper().readValue(captor.getValue(), AuthErrorResponse.class);
        assertEquals("AUTH-401-ALREADY_LOGOUT", error.error());
    }

    @Test
    void commence_withOtherException_returnsDefaultError() throws Exception {
        AuthenticationException ex = mock(AuthenticationException.class);
        entryPoint.commence(request, response, ex);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        AuthErrorResponse error = new ObjectMapper().readValue(captor.getValue(), AuthErrorResponse.class);
        assertEquals("AUTH-401-UNAUTHORIZED", error.error());
    }
}

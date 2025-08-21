package com.example.common.security;

import com.example.common.entity.dto.AuthErrorResponse;
import com.example.common.configuration.security.CustomAccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.access.AccessDeniedException;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CustomAccessDeniedHandlerTest {

    private CustomAccessDeniedHandler handler;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        handler = new CustomAccessDeniedHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void handle_setsForbiddenStatusAndJsonError() throws Exception {
        AccessDeniedException ex = new AccessDeniedException("Denied");
        handler.handle(request, response, ex);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setContentType("application/json");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).write(captor.capture());
        AuthErrorResponse error = new ObjectMapper().readValue(captor.getValue(), AuthErrorResponse.class);
        assertEquals("AUTH-403-ACCESS-DENIED", error.error());
        assertEquals("You don't have permission to access this resource", error.message());
    }
}
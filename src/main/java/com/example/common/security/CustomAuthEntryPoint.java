package com.example.common.security;

import com.example.common.BlackListAuthException;
import com.example.common.entity.dto.AuthErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AuthErrorResponse authErrorResponse =
                switch (authException) {
                    case InsufficientAuthenticationException e ->
                            new AuthErrorResponse("You must login, not enough authentication", "AUTH-401-INSUFFICIENT-AUTH");
                    case BlackListAuthException e ->
                            new AuthErrorResponse("You have expired token, please login again", "AUTH-401-ALREADY_LOGOUT");
                    default -> new AuthErrorResponse("You must login", "AUTH-401-UNAUTHORIZED");

                };
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(authErrorResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}

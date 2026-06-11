package com.example.common.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BlackListAuthException extends AuthenticationException {
    public BlackListAuthException(String message) {
        super(message);
    }
}

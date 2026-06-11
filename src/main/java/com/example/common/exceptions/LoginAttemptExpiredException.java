package com.example.common.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LoginAttemptExpiredException extends AuthenticationException {

    public LoginAttemptExpiredException(String msg) {
        super(msg);
    }
}

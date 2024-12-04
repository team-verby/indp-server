package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthException extends BusinessException {

    public AuthException(String message) {
        super(message, UNAUTHORIZED);
    }
}

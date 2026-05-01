package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(message, FORBIDDEN);
    }
}

package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message, CONFLICT);
    }
}

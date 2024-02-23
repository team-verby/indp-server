package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message, NOT_FOUND);
    }
}

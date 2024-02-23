package com.verby.indp.domain.common.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message, BAD_REQUEST);
    }
}

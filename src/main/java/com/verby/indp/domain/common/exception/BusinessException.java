package com.verby.indp.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public abstract class BusinessException extends RuntimeException {

    private HttpStatusCode statusCode;

    protected BusinessException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

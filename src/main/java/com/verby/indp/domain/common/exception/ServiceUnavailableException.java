package com.verby.indp.domain.common.exception;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

public class ServiceUnavailableException extends BusinessException {
    public ServiceUnavailableException(String message) {
        super(message, SERVICE_UNAVAILABLE);

    }
}

package com.verby.indp.global.exception;

import com.verby.indp.domain.common.exception.ServiceUnavailableException;

public class ExternalApiException extends ServiceUnavailableException {

    public ExternalApiException(String message) {
        super(message);
    }
}

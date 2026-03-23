package com.verby.indp.domain.payment.exception;

import com.verby.indp.domain.common.exception.BadRequestException;

public class PaymentBadRequestException extends BadRequestException {

    public PaymentBadRequestException(String message) {
        super(message);
    }
}

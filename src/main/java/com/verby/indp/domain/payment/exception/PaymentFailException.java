package com.verby.indp.domain.payment.exception;

import com.verby.indp.domain.common.exception.ServiceUnavailableException;

public class PaymentFailException extends ServiceUnavailableException {

    public PaymentFailException(String message) {
        super(message);
    }
}

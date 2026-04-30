package com.verby.indp.domain.payment.exception;

import com.verby.indp.domain.common.exception.ServiceUnavailableException;

public class TossPaymentFailException extends ServiceUnavailableException {

    public TossPaymentFailException(String message) {
        super(message);
    }
}

package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;

public interface PaymentConfirmHandler {

    PaymentType supportedType();

    void handle(Payment payment);
}

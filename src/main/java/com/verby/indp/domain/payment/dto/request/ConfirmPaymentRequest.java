package com.verby.indp.domain.payment.dto.request;

import com.verby.indp.domain.payment.PaymentType;

public record ConfirmPaymentRequest(
    PaymentType paymentType,
    String paymentKey,
    String orderId,
    Integer amount
) {
}

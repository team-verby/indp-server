package com.verby.indp.domain.payment.dto.request;

public record ConfirmPaymentRequest(
    String paymentKey,
    String orderId,
    Integer amount
) {
}

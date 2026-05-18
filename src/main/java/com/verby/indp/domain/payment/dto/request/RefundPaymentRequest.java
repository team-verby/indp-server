package com.verby.indp.domain.payment.dto.request;

public record RefundPaymentRequest(
    int cancelAmount,
    String cancelReason
) {
}

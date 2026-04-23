package com.verby.indp.domain.payment.dto.request;

public record CancelPaymentRequest(
    int cancelAmount,
    String cancelReason
) {
}

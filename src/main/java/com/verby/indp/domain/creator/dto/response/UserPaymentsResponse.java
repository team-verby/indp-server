package com.verby.indp.domain.creator.dto.response;

import java.time.LocalDate;
import java.util.List;

public record UserPaymentsResponse(
    List<PaymentItem> payments
) {

    public record PaymentItem(
        String planName,
        int amount,
        LocalDate paidAt
    ) {

    }
}

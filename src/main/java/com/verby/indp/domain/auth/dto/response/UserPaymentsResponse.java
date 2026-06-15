package com.verby.indp.domain.auth.dto.response;

import com.verby.indp.domain.subscription.UserSubscription;

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

        public static PaymentItem from(UserSubscription subscription) {
            return new PaymentItem(
                subscription.getPlanName(),
                subscription.getMonthlyRate() * subscription.getUsagePeriod(),
                subscription.getPayment().getPaidAt() != null
                    ? subscription.getPayment().getPaidAt().toLocalDate()
                    : null
            );
        }
    }
}

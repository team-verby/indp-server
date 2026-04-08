package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindLatestSubscriptionResponse(
    SubscriptionStatus status,
    String planType,
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime paidAt,
    String subscriptionStatus
) {

    public static FindLatestSubscriptionResponse from(StoreSubscription subscription) {
        return new FindLatestSubscriptionResponse(
            subscription.getStatus(),
            subscription.getPlan().getType().name(),
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.getPayment().getPaidAt(),
            subscription.getStatus().name()
        );
    }
}

package com.verby.indp.domain.subscription.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindSubscriptionsResponse(
    List<SubscriptionItem> subscriptions
) {

    public static FindSubscriptionsResponse from(List<StoreSubscription> subscriptions) {
        return new FindSubscriptionsResponse(
            subscriptions.stream()
                .map(SubscriptionItem::from)
                .toList()
        );
    }

    private record SubscriptionItem(
        LocalDate startDate,
        LocalDate endDate,
        LocalDateTime paidAt,
        String planType
    ) {

        private static SubscriptionItem from(StoreSubscription subscription) {
            LocalDateTime paidAt = subscription.getPayment() != null
                ? subscription.getPayment().getPaidAt()
                : null;

            return new SubscriptionItem(
                subscription.getStartDate(),
                subscription.getEndDate(),
                paidAt,
                subscription.getPlan().getType().name()
            );
        }
    }
}

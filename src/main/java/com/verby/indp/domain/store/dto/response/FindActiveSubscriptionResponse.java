package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record FindActiveSubscriptionResponse(List<SubscriptionItem> subscriptions) {

    private static final Set<SubscriptionStatus> ACTIVE_STATUSES = Set.of(
        SubscriptionStatus.ACTIVE, SubscriptionStatus.PENDING_ACTIVE
    );

    public static FindActiveSubscriptionResponse from(List<StoreSubscription> subscriptions) {
        List<SubscriptionItem> items = subscriptions.stream()
            .filter(s -> ACTIVE_STATUSES.contains(s.getStatus()))
            .map(SubscriptionItem::from)
            .toList();
        return new FindActiveSubscriptionResponse(items);
    }

    public record SubscriptionItem(
        String planType,
        LocalDate startDate,
        LocalDate endDate,
        String status
    ) {

        private static SubscriptionItem from(StoreSubscription subscription) {
            return new SubscriptionItem(
                subscription.getPlan().getType().name(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getStatus().name()
            );
        }
    }
}

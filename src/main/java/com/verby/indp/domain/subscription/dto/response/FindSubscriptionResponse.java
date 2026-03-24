package com.verby.indp.domain.subscription.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindSubscriptionResponse(
    LocalDate startDate,
    LocalDate endDate,
    LocalDateTime paidAt,
    String planCode,
    String planSubtitle
) {

    public static FindSubscriptionResponse from(StoreSubscription subscription) {
        LocalDateTime paidAt = subscription.getPayment() != null
            ? subscription.getPayment().getPaidAt()
            : null;

        return new FindSubscriptionResponse(
            subscription.getStartDate(),
            subscription.getEndDate(),
            paidAt,
            subscription.getPlan().getType(),
            subscription.getPlan().getSubtitle()
        );
    }
}

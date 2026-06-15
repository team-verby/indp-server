package com.verby.indp.domain.auth.dto.response;

import com.verby.indp.domain.subscription.UserSubscription;

import java.time.LocalDate;

public record UserSubscriptionResponse(
    String planName,
    int monthlyRate,
    int usagePeriod,
    LocalDate startDate,
    LocalDate endDate
) {

    public static UserSubscriptionResponse from(UserSubscription subscription) {
        return new UserSubscriptionResponse(
            subscription.getPlanName(),
            subscription.getMonthlyRate(),
            subscription.getUsagePeriod(),
            subscription.getStartDate(),
            subscription.getEndDate()
        );
    }
}

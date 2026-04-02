package com.verby.indp.domain.subscription.dto.request;

public record RenewSubscriptionRequest(
    Long planId,
    int usagePeriod
) {
}

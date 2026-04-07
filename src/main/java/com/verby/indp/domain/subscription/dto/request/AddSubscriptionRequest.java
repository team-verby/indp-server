package com.verby.indp.domain.subscription.dto.request;

public record AddSubscriptionRequest(
    Long planId,
    int usagePeriod
) {

}

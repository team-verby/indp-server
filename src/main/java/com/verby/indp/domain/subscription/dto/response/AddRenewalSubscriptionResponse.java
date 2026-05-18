package com.verby.indp.domain.subscription.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;

public record AddRenewalSubscriptionResponse(
    String orderId,
    int amount,
    String orderName
) {

    public static AddRenewalSubscriptionResponse from(StoreSubscription subscription) {
        return new AddRenewalSubscriptionResponse(
            subscription.getPayment().getOrderId(),
            subscription.getPayment().getTotalAmount(),
            subscription.getPayment().getOrderName()
        );
    }
}

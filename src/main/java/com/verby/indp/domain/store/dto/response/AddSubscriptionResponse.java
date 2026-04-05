package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;

public record AddSubscriptionResponse(
    String orderId,
    int amount,
    String orderName
) {

    public static AddSubscriptionResponse from(StoreSubscription subscription) {
        return new AddSubscriptionResponse(
            subscription.getPayment().getOrderId(),
            subscription.getPayment().getAmount(),
            subscription.getPayment().getOrderName()
        );
    }
}

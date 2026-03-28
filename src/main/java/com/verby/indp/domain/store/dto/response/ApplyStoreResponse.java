package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.subscription.StoreSubscription;

public record ApplyStoreResponse(
    String orderId,
    int amount,
    String orderName
) {

    public static ApplyStoreResponse from(StoreSubscription subscription) {
        return new ApplyStoreResponse(
            subscription.getPayment().getOrderId(),
            subscription.getPayment().getAmount(),
            subscription.getPayment().getOrderName()
        );
    }
}

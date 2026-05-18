package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.subscription.StoreSubscription;

public record AddFirstSubscriptionResponse(
    String orderId,
    int amount,
    String orderName,
    OwnerAccount ownerAccount
) {

    public static AddFirstSubscriptionResponse from(StoreSubscription subscription) {
        return new AddFirstSubscriptionResponse(
            subscription.getPayment().getOrderId(),
            subscription.getPayment().getTotalAmount(),
            subscription.getPayment().getOrderName(),
            OwnerAccount.from(subscription.getStore().getOwner())
        );
    }

    private record OwnerAccount(String loginId, String password) {

        public static OwnerAccount from(Owner owner) {
            return new OwnerAccount(owner.getLoginId(), owner.getPassword());
        }
    }
}

package com.verby.indp.domain.subscription.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.dto.request.RenewSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/owner/subscriptions/")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<FindSubscriptionsResponse> findSubscriptions(@LoginOwner Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(subscriptionService.findSubscriptions(owner, storeId));
    }

    @PostMapping("/stores/{storeId}/renew")
    public ResponseEntity<ApplyStoreResponse> renewSubscription(
        @LoginOwner Owner owner,
        @PathVariable long storeId,
        @RequestBody RenewSubscriptionRequest request
    ) {
        StoreSubscription subscription = subscriptionService.renewSubscription(owner, storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApplyStoreResponse.from(subscription));
    }
}

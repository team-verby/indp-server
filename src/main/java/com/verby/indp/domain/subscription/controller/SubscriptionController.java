package com.verby.indp.domain.subscription.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.subscription.dto.request.AddSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.AddRenewalSubscriptionResponse;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<FindSubscriptionsResponse> findSubscriptions(@LoginOwner Owner owner,
        @PathVariable long storeId) {
        return ResponseEntity.ok(subscriptionService.findSubscriptions(owner, storeId));
    }

    @PostMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<AddRenewalSubscriptionResponse> addSubscription(
        @LoginOwner Owner owner,
        @PathVariable long storeId,
        @RequestBody AddSubscriptionRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(subscriptionService.orderRenewalSubscription(owner, storeId, request));
    }
}

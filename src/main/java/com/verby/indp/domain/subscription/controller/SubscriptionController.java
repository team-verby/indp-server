package com.verby.indp.domain.subscription.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/owner/subscriptions/")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<FindSubscriptionsResponse> findSubscriptions(@LoginOwner Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(subscriptionService.findSubscriptions(owner, storeId));
    }
}

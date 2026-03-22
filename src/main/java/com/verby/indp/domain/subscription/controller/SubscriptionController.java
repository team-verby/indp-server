package com.verby.indp.domain.subscription.controller;

import com.verby.indp.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Subscription endpoints are handled in OwnerStoreController
@RestController
@RequestMapping("/api/owner/stores/{storeId}/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
}

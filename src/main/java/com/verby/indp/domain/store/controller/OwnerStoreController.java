package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindOwnerStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoreListResponse;
import com.verby.indp.domain.store.service.OwnerStoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionResponse;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/stores")
@RequiredArgsConstructor
public class OwnerStoreController {

    private final OwnerStoreService ownerStoreService;
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<FindStoreListResponse> findMyStores(
        @RequestAttribute("ownerId") Long ownerId
    ) {
        List<Store> stores = ownerStoreService.findMyStores(ownerId);
        return ResponseEntity.ok(FindStoreListResponse.from(stores));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<FindOwnerStoreResponse> findMyStore(
        @RequestAttribute("ownerId") Long ownerId,
        @PathVariable long storeId
    ) {
        Store store = ownerStoreService.findMyStore(ownerId, storeId);
        return ResponseEntity.ok(FindOwnerStoreResponse.from(store));
    }

    @GetMapping("/{storeId}/subscriptions")
    public ResponseEntity<List<FindSubscriptionResponse>> findSubscriptions(
        @RequestAttribute("ownerId") Long ownerId,
        @PathVariable long storeId
    ) {
        ownerStoreService.findMyStore(ownerId, storeId); // ownership check
        List<StoreSubscription> subscriptions = subscriptionService.findSubscriptions(storeId);
        List<FindSubscriptionResponse> responses = subscriptions.stream()
            .map(FindSubscriptionResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }
}

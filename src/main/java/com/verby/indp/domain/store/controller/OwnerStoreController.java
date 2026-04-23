package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.dto.response.FindLatestSubscriptionResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByOwnerResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByOwnerResponse;
import com.verby.indp.domain.store.service.OwnerStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner/stores")
@RequiredArgsConstructor
public class OwnerStoreController {

    private final OwnerStoreService ownerStoreService;

    @GetMapping
    public ResponseEntity<FindStoresByOwnerResponse> findMyStores(@RequestAttribute("owner") Owner owner) {
        return ResponseEntity.ok(ownerStoreService.getMyStores(owner));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<FindStoreByOwnerResponse> findMyStore(
        @RequestAttribute("owner") Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(ownerStoreService.getMyStore(owner, storeId));
    }

    @GetMapping("/{storeId}/subscription")
    public ResponseEntity<FindLatestSubscriptionResponse> findLatestSubscription(
        @RequestAttribute("owner") Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(ownerStoreService.getLatestSubscription(owner, storeId));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(
        @RequestAttribute("owner") Owner owner,
        @PathVariable long storeId,
        @RequestBody UpdateStoreRequest request
    ) {
        ownerStoreService.updateStore(owner, storeId, request);
        return ResponseEntity.noContent().build();
    }
}

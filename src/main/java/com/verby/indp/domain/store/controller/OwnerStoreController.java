package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.store.dto.response.FindOwnerStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
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
    public ResponseEntity<FindStoresResponse> findMyStores(@RequestAttribute("ownerId") Owner owner) {
        return ResponseEntity.ok(ownerStoreService.getMyStores(owner));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<FindOwnerStoreResponse> findMyStore(@RequestAttribute("ownerId") Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(ownerStoreService.getMyStore(owner, storeId));
    }
}

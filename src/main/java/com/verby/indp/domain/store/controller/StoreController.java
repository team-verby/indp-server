package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.AddSubscriptionResponse;
import com.verby.indp.domain.store.dto.response.FindStoreSummaryResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.service.ApplyStoreService;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final ApplyStoreService applyStoreService;

    @PostMapping("/apply")
    public ResponseEntity<AddSubscriptionResponse> applyStore(
        @RequestBody ApplyStoreRequest request) {
        AddSubscriptionResponse response = applyStoreService.applyStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<FindStoresResponse> findStores(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(storeService.findStores(pageable));
    }

    @GetMapping("/{storeId}/summary")
    public ResponseEntity<FindStoreSummaryResponse> findStoreSummary(@PathVariable long storeId) {
        return ResponseEntity.ok(storeService.findStoreSummary(storeId));
    }
}

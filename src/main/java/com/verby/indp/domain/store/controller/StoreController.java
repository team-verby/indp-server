package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
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

    @PostMapping("/apply")
    public ResponseEntity<ApplyStoreResponse> applyStore(@RequestBody ApplyStoreRequest request) {
        ApplyStoreResponse response = storeService.applyStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<FindStoresResponse> findStores(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(storeService.findStores(pageable));
    }
}

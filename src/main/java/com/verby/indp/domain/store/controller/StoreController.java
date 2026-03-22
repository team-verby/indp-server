package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoreDetailResponse;
import com.verby.indp.domain.store.dto.response.FindStoreListResponse;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final SongRecommendationService songRecommendationService;

    @PostMapping("/apply")
    public ResponseEntity<ApplyStoreResponse> applyStore(@RequestBody ApplyStoreRequest request) {
        ApplyStoreResponse response = storeService.applyStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<FindStoreListResponse> findStores(
        @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<Store> stores = storeService.findStores(pageable);
        return ResponseEntity.ok(FindStoreListResponse.from(stores.getContent()));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<FindStoreDetailResponse> findStore(@PathVariable long storeId) {
        Store store = storeService.findStore(storeId);
        return ResponseEntity.ok(FindStoreDetailResponse.from(store));
    }
}

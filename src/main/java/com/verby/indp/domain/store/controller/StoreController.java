package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.dto.request.AddStoreByAdminRequest;
import com.verby.indp.domain.store.dto.request.UpdateStoreByAdminRequest;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.service.StoreService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/main/stores")
    public ResponseEntity<FindSimpleStoresResponse> findSimpleStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.findSimpleStores(pageable));
    }

    @GetMapping("/stores")
    public ResponseEntity<FindStoresResponse> findStores(
        Pageable pageable,
        @RequestParam(name = "region", required = false) String region
    ) {
        return ResponseEntity.ok(storeService.findStores(pageable, region));
    }

    @GetMapping("/admin/stores")
    public ResponseEntity<FindStoresByAdminResponse> findStoresByAdmin(
        Pageable pageable
    ) {
        return ResponseEntity.ok(storeService.findStoresByAdmin(pageable));
    }

    @GetMapping("/admin/stores/{storeId}")
    public ResponseEntity<FindStoreByAdminResponse> findStoreByAdmin(@PathVariable(name = "storeId") Long storeId) {
        return ResponseEntity.ok(storeService.findStoreByAdmin(storeId));
    }

    @DeleteMapping("/admin/stores/{storeId}")
    public ResponseEntity<Void> deleteStoreByAdmin(@PathVariable(name = "storeId") Long storeId) {
        storeService.deleteStoreByAdmin(storeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/stores")
    public ResponseEntity<Void> addStoreByAdmin(@RequestBody AddStoreByAdminRequest request) {
        long storeId = storeService.addStore(request);
        URI location = URI.create("/api/admin/stores/" + storeId);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/admin/stores/{storeId}")
    public ResponseEntity<Void> updateStoreByAdmin(@PathVariable(name = "storeId") Long storeId, @RequestBody UpdateStoreByAdminRequest request) {
        storeService.updateStore(storeId, request);
        return ResponseEntity.ok().build();
    }

}

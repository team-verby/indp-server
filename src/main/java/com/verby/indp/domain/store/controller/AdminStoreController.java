package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.service.AdminStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stores")
@RequiredArgsConstructor
public class AdminStoreController {

    private final AdminStoreService storeService;

    @GetMapping
    public ResponseEntity<FindStoresByAdminResponse> findStores(
        @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(storeService.findStores(pageable));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<FindStoreByAdminResponse> findStore(@PathVariable long storeId) {
        return ResponseEntity.ok(storeService.findStore(storeId));
    }
}

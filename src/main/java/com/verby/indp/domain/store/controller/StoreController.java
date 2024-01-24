package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/main/stores")
    public ResponseEntity<FindSimpleStoresResponse> findSimpleStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.findSimpleStores(pageable));
    }

    @GetMapping("/stores")
    public ResponseEntity<FindStoresResponse> findSimpleStores(
        Pageable pageable,
        @RequestParam(name = "region", required = false) Region region
    ) {
        return ResponseEntity.ok(storeService.findStores(pageable, region));
    }

}

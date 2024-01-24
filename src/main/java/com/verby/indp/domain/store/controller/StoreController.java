package com.verby.indp.domain.store.controller;

import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/main/stores")
    public ResponseEntity<FindSimpleStoresResponse> findSimpleStores(Pageable pageable) {
        return ResponseEntity.ok(storeService.findSimpleStores(pageable));
    }

}

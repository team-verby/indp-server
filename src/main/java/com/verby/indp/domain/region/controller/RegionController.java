package com.verby.indp.domain.region.controller;

import com.verby.indp.domain.region.service.RegionService;
import com.verby.indp.domain.store.dto.response.FindRegionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/regions")
    public ResponseEntity<FindRegionsResponse> findRegions() {
        return ResponseEntity.ok(regionService.findRegions());
    }
}

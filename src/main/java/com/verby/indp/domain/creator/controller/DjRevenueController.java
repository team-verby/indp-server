package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import com.verby.indp.domain.creator.service.DjRevenueService;
import com.verby.indp.global.resolver.LoginCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dj")
@RequiredArgsConstructor
public class DjRevenueController {

    private final DjRevenueService djRevenueService;

    @GetMapping("/revenue")
    public ResponseEntity<DjRevenueResponse> getRevenue(@LoginCreator Creator creator) {
        return ResponseEntity.ok(djRevenueService.getRevenue(creator));
    }
}

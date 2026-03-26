package com.verby.indp.domain.plan.controller;

import com.verby.indp.domain.plan.dto.response.FindPlansResponse;
import com.verby.indp.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<FindPlansResponse> findPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }
}

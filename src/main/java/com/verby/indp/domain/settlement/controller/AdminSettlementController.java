package com.verby.indp.domain.settlement.controller;

import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse;
import com.verby.indp.domain.settlement.dto.response.SettlementTaxSecretResponse;
import com.verby.indp.domain.settlement.service.AdminSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/settlements")
@RequiredArgsConstructor
public class AdminSettlementController {

    private final AdminSettlementService adminSettlementService;

    @GetMapping
    public ResponseEntity<FindSettlementsResponse> findSettlements(
        @RequestParam(required = false) SettlementStatus status) {
        return ResponseEntity.ok(adminSettlementService.findSettlements(status));
    }

    @GetMapping("/{settlementRequestId}/tax-info")
    public ResponseEntity<SettlementTaxSecretResponse> getTaxSecret(
        @PathVariable long settlementRequestId) {
        return ResponseEntity.ok(adminSettlementService.getTaxSecret(settlementRequestId));
    }

    @PatchMapping("/{settlementRequestId}/approve")
    public ResponseEntity<Void> approve(@PathVariable long settlementRequestId) {
        adminSettlementService.approve(settlementRequestId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{settlementRequestId}/reject")
    public ResponseEntity<Void> reject(@PathVariable long settlementRequestId) {
        adminSettlementService.reject(settlementRequestId);
        return ResponseEntity.ok().build();
    }
}

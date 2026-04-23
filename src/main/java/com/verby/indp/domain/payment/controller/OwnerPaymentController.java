package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.payment.dto.response.FindOwnerPaymentsResponse;
import com.verby.indp.domain.payment.service.OwnerPaymentService;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/payments")
public class OwnerPaymentController {

    private final OwnerPaymentService ownerPaymentService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<FindOwnerPaymentsResponse> findPayments(
        @LoginOwner Owner owner,
        @PathVariable long storeId,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(ownerPaymentService.findPayments(owner, storeId, pageable));
    }
}

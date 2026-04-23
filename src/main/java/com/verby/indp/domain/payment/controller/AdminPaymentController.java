package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.payment.dto.request.CancelPaymentRequest;
import com.verby.indp.domain.payment.dto.response.FindAdminPaymentsResponse;
import com.verby.indp.domain.payment.service.AdminPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/payments")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<FindAdminPaymentsResponse> findPayments(
        @PathVariable long storeId,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(adminPaymentService.findPayments(storeId, pageable));
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable long paymentId, @RequestBody CancelPaymentRequest request) {
        adminPaymentService.cancelPayment(paymentId, request);
        return ResponseEntity.ok().build();
    }
}

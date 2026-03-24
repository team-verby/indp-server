package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.dto.request.FailPaymentRequest;
import com.verby.indp.domain.payment.service.PaymentService;
import com.verby.indp.domain.store.dto.response.ConfirmApplyPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmApplyPaymentResponse> confirmApplyPayment(
        @RequestBody ConfirmPaymentRequest request
    ) {
        paymentService.confirm(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/fail")
    public ResponseEntity<Void> failApplyPayment(
        @RequestBody FailPaymentRequest request
    ) {
        paymentService.failPayment(request.orderId());
        return ResponseEntity.ok().build();
    }
}

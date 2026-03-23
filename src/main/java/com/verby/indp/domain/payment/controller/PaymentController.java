package com.verby.indp.domain.payment.controller;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.dto.request.FailPaymentRequest;
import com.verby.indp.domain.payment.service.PaymentService;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.dto.response.ConfirmApplyPaymentResponse;
import com.verby.indp.domain.store.service.StoreService;
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
    private final StoreService storeService;
    private final SongRecommendationService songRecommendationService;

    @PostMapping("/apply/confirm")
    public ResponseEntity<ConfirmApplyPaymentResponse> confirmApplyPayment(
        @RequestBody ConfirmPaymentRequest request
    ) {
        Payment payment = paymentService.validateAndConfirmPayment(
            request.orderId(), request.paymentKey(), request.amount());
        return ResponseEntity.ok(storeService.confirmApplyPayment(payment));
    }

    @PostMapping("/apply/fail")
    public ResponseEntity<Void> failApplyPayment(
        @RequestBody FailPaymentRequest request
    ) {
        paymentService.failPayment(request.orderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recommendation/confirm")
    public ResponseEntity<Void> confirmRecommendationPayment(
        @RequestBody ConfirmPaymentRequest request
    ) {
        Payment payment = paymentService.validateAndConfirmPayment(
            request.orderId(), request.paymentKey(), request.amount());
        songRecommendationService.confirmPayment(payment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recommendation/fail")
    public ResponseEntity<Void> failRecommendationPayment(
        @RequestBody FailPaymentRequest request
    ) {
        paymentService.failPayment(request.orderId());
        return ResponseEntity.ok().build();
    }
}

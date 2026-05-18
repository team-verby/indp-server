package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.service.PaymentConfirmHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SongRecommendationPaymentConfirmHandler implements PaymentConfirmHandler {

    private final SongRecommendationService songRecommendationService;

    @Override
    public PaymentType supportedType() {
        return PaymentType.SONG_RECOMMENDATION;
    }

    @Override
    public void handle(Payment payment) {
        songRecommendationService.confirmPayment(payment);
    }
}

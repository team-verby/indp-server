package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.service.PaymentConfirmHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionPaymentConfirmHandler implements PaymentConfirmHandler {

    private final SubscriptionService subscriptionService;

    @Override
    public PaymentType supportedType() {
        return PaymentType.SUBSCRIPTION;
    }

    @Override
    public void handle(Payment payment) {
        subscriptionService.confirmPayment(payment);
    }
}

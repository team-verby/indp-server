package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.service.PaymentConfirmHandler;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserSubscriptionPaymentConfirmHandler implements PaymentConfirmHandler {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final Clock clock;

    @Override
    public PaymentType supportedType() {
        return PaymentType.USER_SUBSCRIPTION;
    }

    @Override
    public void handle(Payment payment) {
        UserSubscription subscription = userSubscriptionRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("Plan A 구독 정보가 존재하지 않습니다."));
        subscription.activate(LocalDate.now(clock));
    }
}

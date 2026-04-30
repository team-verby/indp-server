package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SubscriptionPaymentConfirmHandlerTest {

    @InjectMocks
    private SubscriptionPaymentConfirmHandler handler;

    @Mock
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("supportedType은 SUBSCRIPTION을 반환한다.")
    void supportedType() {
        assertThat(handler.supportedType()).isEqualTo(PaymentType.SUBSCRIPTION);
    }

    @Test
    @DisplayName("handle 호출 시 subscriptionService.confirmPayment를 위임한다.")
    void handle() {
        Payment payment = new Payment(PaymentType.SUBSCRIPTION, "인디피_구독_카페공명", 180000);
        willDoNothing().given(subscriptionService).confirmPayment(payment);

        handler.handle(payment);

        verify(subscriptionService).confirmPayment(payment);
    }
}

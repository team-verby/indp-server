package com.verby.indp.domain.payment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private SongRecommendationService songRecommendationService;

    @Mock
    private SubscriptionService subscriptionService;

    @Nested
    @DisplayName("confirm 메서드 실행 시")
    class Confirm {

        @Test
        @DisplayName("성공 : 구독 결제를 확인한다.")
        void confirmSubscriptionPayment() {
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));
            willDoNothing().given(subscriptionService).confirmPayment(payment);
            willDoNothing().given(paymentClient).confirmPayment(orderId, "payment-key", 180000);

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 180000);

            Exception exception = catchException(() -> paymentService.confirm(request));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 음악 추천 결제를 확인한다.")
        void confirmSongRecommendationPayment() {
            Payment payment = new Payment("인디피_추천_카페공명", 3000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));
            willDoNothing().given(songRecommendationService).confirmPayment(payment);
            willDoNothing().given(paymentClient).confirmPayment(orderId, "payment-key", 3000);

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SONG_RECOMMENDATION,
                "payment-key", orderId, 3000);

            Exception exception = catchException(() -> paymentService.confirm(request));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 결제 정보가 없으면 예외를 던진다.")
        void confirmWithNotFound() {
            given(paymentRepository.findByOrderId("unknown")).willReturn(Optional.empty());

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", "unknown", 180000);

            Exception exception = catchException(() -> paymentService.confirm(request));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 이미 처리된 결제이면 예외를 던진다.")
        void confirmWithAlreadyProcessed() {
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            payment.success();
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 180000);

            Exception exception = catchException(() -> paymentService.confirm(request));

            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 결제 금액이 다르면 예외를 던진다.")
        void confirmWithDifferentAmount() {
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 100000);

            Exception exception = catchException(() -> paymentService.confirm(request));

            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }

    @Nested
    @DisplayName("failPayment 메서드 실행 시")
    class FailPayment {

        @Test
        @DisplayName("성공 : 결제를 실패 처리한다.")
        void failPayment() {
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            Exception exception = catchException(() -> paymentService.failPayment(orderId));

            assertThat(exception).isNull();
            assertThat(payment.isFail()).isTrue();
        }

        @Test
        @DisplayName("실패 : 결제 정보가 없으면 예외를 던진다.")
        void failPaymentWithNotFound() {
            given(paymentRepository.findByOrderId("unknown")).willReturn(Optional.empty());

            Exception exception = catchException(() -> paymentService.failPayment("unknown"));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 이미 처리된 결제이면 예외를 던진다.")
        void failPaymentWithAlreadyProcessed() {
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            payment.fail();
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            Exception exception = catchException(() -> paymentService.failPayment(orderId));

            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }
}

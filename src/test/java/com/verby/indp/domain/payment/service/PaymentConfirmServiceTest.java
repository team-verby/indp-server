package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.exception.PaymentFailException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.lenient;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class PaymentConfirmServiceTest {

    @InjectMocks
    private PaymentConfirmService paymentConfirmService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private SongRecommendationService songRecommendationService;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        lenient().when(clock.instant()).thenReturn(Instant.parse("2026-04-24T03:00:00Z"));
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Nested
    @DisplayName("confirm 메서드 실행 시")
    class Confirm {

        @Test
        @DisplayName("성공 : 구독 결제를 확인한다.")
        void confirmSubscriptionPayment() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));
            willDoNothing().given(subscriptionService).confirmPayment(payment);
            willDoNothing().given(paymentClient).confirmPayment(orderId, "payment-key", 180000);

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 180000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 음악 추천 결제를 확인한다.")
        void confirmSongRecommendationPayment() {
            // given
            Payment payment = new Payment("인디피_추천_카페공명", 3000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));
            willDoNothing().given(songRecommendationService).confirmPayment(payment);
            willDoNothing().given(paymentClient).confirmPayment(orderId, "payment-key", 3000);

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SONG_RECOMMENDATION,
                "payment-key", orderId, 3000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : Toss API 실패 시 결제를 ABORTED로 변경한다.")
        void confirmWithTossApiFail() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));
            willDoNothing().given(subscriptionService).confirmPayment(payment);
            willThrow(new PaymentFailException("잔액 부족")).given(paymentClient)
                .confirmPayment(orderId, "payment-key", 180000);
            willDoNothing().given(paymentService).failPayment(orderId);

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 180000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isInstanceOf(PaymentFailException.class);
        }

        @Test
        @DisplayName("실패 : 결제 정보가 없으면 예외를 던진다.")
        void confirmWithNotFound() {
            // given
            given(paymentRepository.findByOrderId("unknown")).willReturn(Optional.empty());

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", "unknown", 180000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 이미 처리된 결제이면 예외를 던진다.")
        void confirmWithAlreadyProcessed() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            payment.success(LocalDateTime.now());
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 180000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 결제 금액이 다르면 예외를 던진다.")
        void confirmWithDifferentAmount() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            ConfirmPaymentRequest request = new ConfirmPaymentRequest(PaymentType.SUBSCRIPTION,
                "payment-key", orderId, 100000);

            // when
            Exception exception = catchException(() -> paymentConfirmService.confirm(request));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }

}

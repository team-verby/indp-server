package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Nested
    @DisplayName("failPayment 메서드 실행 시")
    class FailPayment {

        @Test
        @DisplayName("성공 : 결제를 실패 처리한다.")
        void failPayment() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            // when
            Exception exception = catchException(() -> paymentService.failPayment(orderId));

            // then
            assertThat(exception).isNull();
            assertThat(payment.isStatusWith(PaymentStatus.ABORTED)).isTrue();
        }

        @Test
        @DisplayName("실패 : 결제 정보가 없으면 예외를 던진다.")
        void failPaymentWithNotFound() {
            // given
            given(paymentRepository.findByOrderId("unknown")).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> paymentService.failPayment("unknown"));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 이미 처리된 결제이면 예외를 던진다.")
        void failPaymentWithAlreadyProcessed() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);
            payment.fail();
            String orderId = payment.getOrderId();
            given(paymentRepository.findByOrderId(orderId)).willReturn(Optional.of(payment));

            // when
            Exception exception = catchException(() -> paymentService.failPayment(orderId));

            // then
            assertThat(exception).isInstanceOf(PaymentBadRequestException.class);
        }
    }
}

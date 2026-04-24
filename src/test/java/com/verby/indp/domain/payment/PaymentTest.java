package com.verby.indp.domain.payment;

import com.verby.indp.domain.common.exception.BadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class PaymentTest {

    @Nested
    @DisplayName("Payment 생성 시")
    class NewPayment {

        @Test
        @DisplayName("성공 : Payment를 생성한다.")
        void newPayment() {
            // when
            Exception exception = catchException(
                () -> new Payment("인디피_구독_카페공명", 180000));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : orderName이 null이면 예외를 던진다.")
        void newPaymentWithNullOrderName() {
            // when
            Exception exception = catchException(() -> new Payment(null, 180000));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : orderName이 blank이면 예외를 던진다.")
        void newPaymentWithBlankOrderName() {
            // when
            Exception exception = catchException(() -> new Payment("  ", 180000));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : amount가 0 이하이면 예외를 던진다.")
        void newPaymentWithZeroAmount() {
            // when
            Exception exception = catchException(
                () -> new Payment("인디피_구독_카페공명", 0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : amount가 음수이면 예외를 던진다.")
        void newPaymentWithNegativeAmount() {
            // when
            Exception exception = catchException(
                () -> new Payment("인디피_구독_카페공명", -1000));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("isDifferentAmount 메서드 실행 시")
    class IsDifferentAmount {

        @Test
        @DisplayName("성공 : 금액이 일치하면 false를 반환한다.")
        void isDifferentAmountFalse() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            boolean result = payment.isDifferentAmount(180000);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 : 금액이 다르면 true를 반환한다.")
        void isDifferentAmountTrue() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            boolean result = payment.isDifferentAmount(100000);

            // then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("success 메서드 실행 시")
    class Success {

        @Test
        @DisplayName("성공 : 결제 상태가 DONE으로 변경된다.")
        void success() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            payment.success(LocalDateTime.now());

            // then
            assertThat(payment.isStatusWith(PaymentStatus.DONE)).isTrue();
        }
    }

    @Nested
    @DisplayName("fail 메서드 실행 시")
    class Fail {

        @Test
        @DisplayName("성공 : 결제 상태가 ABORTED로 변경된다.")
        void fail() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            payment.fail();

            // then
            assertThat(payment.isStatusWith(PaymentStatus.ABORTED)).isTrue();
        }
    }

    @Nested
    @DisplayName("cancel 메서드 실행 시")
    class Cancel {

        @Test
        @DisplayName("성공 : 결제 상태가 CANCELED로 변경된다.")
        void cancel() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            payment.cancel(PaymentStatus.CANCELED, 0);

            // then
            assertThat(payment.isStatusWith(PaymentStatus.CANCELED)).isTrue();
            assertThat(payment.getBalanceAmount()).isZero();
        }

        @Test
        @DisplayName("성공 : 부분 취소 시 결제 상태가 PARTIAL_CANCELED로 변경된다.")
        void partialCancel() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            payment.cancel(PaymentStatus.PARTIAL_CANCELED, 90000);

            // then
            assertThat(payment.isStatusWith(PaymentStatus.PARTIAL_CANCELED)).isTrue();
            assertThat(payment.getBalanceAmount()).isEqualTo(90000);
        }
    }

    @Nested
    @DisplayName("updatePaymentKey 메서드 실행 시")
    class UpdatePaymentKey {

        @Test
        @DisplayName("성공 : 결제 키를 업데이트한다.")
        void updatePaymentKey() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            Exception exception = catchException(() -> payment.updatePaymentKey("payment-key"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : paymentKey가 null이면 예외를 던진다.")
        void updatePaymentKeyWithNull() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            Exception exception = catchException(() -> payment.updatePaymentKey(null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : paymentKey가 blank이면 예외를 던진다.")
        void updatePaymentKeyWithBlank() {
            // given
            Payment payment = new Payment("인디피_구독_카페공명", 180000);

            // when
            Exception exception = catchException(() -> payment.updatePaymentKey("  "));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

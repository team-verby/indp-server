package com.verby.indp.domain.subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.fixture.PaymentFixture;
import com.verby.indp.fixture.PlanFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StoreSubscriptionTest {

    private Plan plan() {
        return PlanFixture.planA();
    }

    private Payment payment() {
        return PaymentFixture.payment();
    }

    @Nested
    @DisplayName("StoreSubscription 생성 시")
    class NewStoreSubscription {

        @Test
        @DisplayName("성공 : StoreSubscription을 생성한다.")
        void newStoreSubscription() {
            // given & when
            Exception exception = catchException(() ->
                new StoreSubscription(plan(), payment(), 12, LocalDate.of(2026, 1, 12)));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : plan이 null이면 예외를 던진다.")
        void newStoreSubscriptionWithNullPlan() {
            // given & when
            Exception exception = catchException(() ->
                new StoreSubscription(null, payment(), 12, LocalDate.of(2026, 1, 12)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : payment가 null이면 예외를 던진다.")
        void newStoreSubscriptionWithNullPayment() {
            // given & when
            Exception exception = catchException(() ->
                new StoreSubscription(plan(), null, 12, LocalDate.of(2026, 1, 12)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : usagePeriod가 0 이하이면 예외를 던진다.")
        void newStoreSubscriptionWithNonPositiveUsagePeriod() {
            // given & when
            Exception exception = catchException(() ->
                new StoreSubscription(plan(), payment(), 0, LocalDate.of(2026, 1, 12)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : startDate가 null이면 예외를 던진다.")
        void newStoreSubscriptionWithNullStartDate() {
            // give & when
            Exception exception = catchException(() ->
                new StoreSubscription(plan(), payment(), 12, null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("updateStatus 메서드 실행 시")
    class UpdateStatus {

        @Test
        @DisplayName("성공 : 구독 상태를 변경한다.")
        void updateStatus() {
            // given
            StoreSubscription subscription = new StoreSubscription(
                plan(), payment(), 12, LocalDate.of(2026, 1, 12));

            // when
            subscription.updateStatus(SubscriptionStatus.ACTIVE);

            // then
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        }
    }
}

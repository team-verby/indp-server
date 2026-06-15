package com.verby.indp.domain.subscription;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserSubscriptionTest {

    @Nested
    @DisplayName("UserSubscription 생성 시")
    class NewUserSubscription {

        @Test
        @DisplayName("성공 : PENDING_PAYMENT 상태로 생성된다.")
        void create() {
            User user = userWithId(1L);
            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A 라이트 요금제 · 월간", 4400);
            UserSubscription sub = new UserSubscription(user, payment, 1);
            assertThat(sub.getStatus()).isEqualTo(UserSubscriptionStatus.PENDING_PAYMENT);
            assertThat(sub.getPlanName()).isEqualTo("Plan A 라이트 요금제");
            assertThat(sub.getMonthlyRate()).isEqualTo(4400);
        }
    }

    @Nested
    @DisplayName("activate 메서드 실행 시")
    class Activate {

        @Test
        @DisplayName("성공 : 월간 구독 활성화 시 endDate = startDate + 1개월 - 1일.")
        void activateMonthly() {
            User user = userWithId(1L);
            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            UserSubscription sub = new UserSubscription(user, payment, 1);
            LocalDate today = LocalDate.of(2026, 6, 15);
            sub.activate(today);
            assertThat(sub.getStatus()).isEqualTo(UserSubscriptionStatus.ACTIVE);
            assertThat(sub.getStartDate()).isEqualTo(today);
            assertThat(sub.getEndDate()).isEqualTo(LocalDate.of(2026, 7, 14));
        }

        @Test
        @DisplayName("성공 : 연간 구독 활성화 시 endDate = startDate + 12개월 - 1일.")
        void activateAnnual() {
            User user = userWithId(1L);
            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A · 연간", 52800);
            UserSubscription sub = new UserSubscription(user, payment, 12);
            LocalDate today = LocalDate.of(2026, 6, 15);
            sub.activate(today);
            assertThat(sub.getEndDate()).isEqualTo(LocalDate.of(2027, 6, 14));
        }
    }
}

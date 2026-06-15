package com.verby.indp.domain.subscription.service;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionPaymentConfirmHandlerTest {

    @InjectMocks
    private UserSubscriptionPaymentConfirmHandler handler;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("handle 메서드 실행 시")
    class Handle {

        @Test
        @DisplayName("성공 : 구독을 ACTIVE로 활성화한다.")
        void handle() {
            User user = userWithId(1L);
            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            UserSubscription sub = new UserSubscription(user, payment, 1);
            given(userSubscriptionRepository.findByPayment(any())).willReturn(Optional.of(sub));
            given(clock.instant()).willReturn(Instant.parse("2026-06-15T00:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));

            handler.handle(payment);

            assertThat(sub.getStatus()).isEqualTo(UserSubscriptionStatus.ACTIVE);
            assertThat(sub.getStartDate()).isEqualTo(LocalDate.of(2026, 6, 15));
        }

        @Test
        @DisplayName("실패 : 구독 정보가 없으면 예외를 던진다.")
        void handleNotFound() {
            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            given(userSubscriptionRepository.findByPayment(any())).willReturn(Optional.empty());

            Exception exception = catchException(() -> handler.handle(payment));
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

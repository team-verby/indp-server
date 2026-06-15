package com.verby.indp.domain.auth.service;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.response.UserPaymentsResponse;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionServiceTest {

    @InjectMocks
    private UserSubscriptionService userSubscriptionService;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Nested
    @DisplayName("getSubscription 메서드 실행 시")
    class GetSubscription {

        @Test
        @DisplayName("실패 : 활성 구독이 없으면 NotFoundException을 던진다.")
        void getSubscriptionNotFound() {
            User user = userWithId(1L);
            given(userSubscriptionRepository.findTopByUserAndStatusOrderByCreatedAtDesc(
                any(), eq(UserSubscriptionStatus.ACTIVE))).willReturn(Optional.empty());

            Exception exception = catchException(
                () -> userSubscriptionService.getSubscription(user));
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getSubscription 성공 시")
    class GetSubscriptionSuccess {

        @Test
        @DisplayName("성공 : 활성 구독 정보를 반환한다.")
        void getSubscription() {
            User user = userWithId(1L);
            com.verby.indp.domain.payment.Payment payment = new com.verby.indp.domain.payment.Payment(
                com.verby.indp.domain.payment.PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            com.verby.indp.domain.subscription.UserSubscription sub =
                new com.verby.indp.domain.subscription.UserSubscription(user, payment, 1);
            sub.activate(java.time.LocalDate.of(2026, 6, 15));

            given(userSubscriptionRepository.findTopByUserAndStatusOrderByCreatedAtDesc(
                any(), eq(UserSubscriptionStatus.ACTIVE))).willReturn(Optional.of(sub));

            var response = userSubscriptionService.getSubscription(user);
            assertThat(response.planName()).isEqualTo("Plan A 라이트 요금제");
            assertThat(response.monthlyRate()).isEqualTo(4400);
        }
    }

    @Nested
    @DisplayName("getPayments 메서드 실행 시")
    class GetPayments {

        @Test
        @DisplayName("성공 : 결제 완료된 구독은 결제 내역에 포함된다.")
        void getPaymentsWithPaidSubscription() {
            User user = userWithId(1L);
            com.verby.indp.domain.payment.Payment payment = new com.verby.indp.domain.payment.Payment(
                com.verby.indp.domain.payment.PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            payment.success(java.time.LocalDateTime.of(2026, 6, 15, 12, 0));
            com.verby.indp.domain.subscription.UserSubscription sub =
                new com.verby.indp.domain.subscription.UserSubscription(user, payment, 1);

            given(userSubscriptionRepository.findAllByUserOrderByCreatedAtDesc(any()))
                .willReturn(List.of(sub));

            UserPaymentsResponse response = userSubscriptionService.getPayments(user);
            assertThat(response.payments()).hasSize(1);
            assertThat(response.payments().get(0).amount()).isEqualTo(4400);
        }

        @Test
        @DisplayName("성공 : 결제 미완료 구독은 결제 내역에서 제외된다.")
        void getPaymentsExcludesPending() {
            User user = userWithId(1L);
            com.verby.indp.domain.payment.Payment payment = new com.verby.indp.domain.payment.Payment(
                com.verby.indp.domain.payment.PaymentType.USER_SUBSCRIPTION, "Plan A · 월간", 4400);
            // paidAt 미설정 (PENDING 상태)
            com.verby.indp.domain.subscription.UserSubscription sub =
                new com.verby.indp.domain.subscription.UserSubscription(user, payment, 1);

            given(userSubscriptionRepository.findAllByUserOrderByCreatedAtDesc(any()))
                .willReturn(List.of(sub));

            UserPaymentsResponse response = userSubscriptionService.getPayments(user);
            assertThat(response.payments()).isEmpty();
        }
    }
}

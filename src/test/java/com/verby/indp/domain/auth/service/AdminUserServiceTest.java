package com.verby.indp.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.dto.response.FindUsersResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import com.verby.indp.fixture.UserFixture;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @InjectMocks
    private AdminUserService adminUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Nested
    @DisplayName("findUsers 메서드 실행 시")
    class FindUsers {

        @Test
        @DisplayName("성공 : 구독이 있는 사용자 목록을 반환한다.")
        void findUsersWithSubscription() {
            var user = UserFixture.user();
            var sub = org.mockito.Mockito.mock(UserSubscription.class);
            var payment = org.mockito.Mockito.mock(com.verby.indp.domain.payment.Payment.class);
            given(payment.getTotalAmount()).willReturn(4400);
            given(payment.getPaidAt()).willReturn(java.time.LocalDateTime.of(2026, 6, 15, 12, 0));
            given(sub.getStatus()).willReturn(com.verby.indp.domain.subscription.UserSubscriptionStatus.ACTIVE);
            given(sub.getStartDate()).willReturn(java.time.LocalDate.of(2026, 6, 15));
            given(sub.getEndDate()).willReturn(java.time.LocalDate.of(2026, 7, 14));
            given(sub.getPayment()).willReturn(payment);
            given(sub.getUsagePeriod()).willReturn(1);

            given(userRepository.findAll()).willReturn(List.of(user));
            given(userSubscriptionRepository.findAllByUserOrderByCreatedAtDesc(user))
                .willReturn(List.of(sub));

            FindUsersResponse response = adminUserService.findUsers();

            assertThat(response.users()).hasSize(1);
            assertThat(response.users().get(0).subscriptionStatus()).isEqualTo("ACTIVE");
            assertThat(response.users().get(0).subscriptionEndDate()).isEqualTo(java.time.LocalDate.of(2026, 7, 14));
        }

        @Test
        @DisplayName("성공 : 구독이 없는 사용자는 NO_SUBSCRIPTION으로 반환한다.")
        void findUsersWithNoSubscription() {
            var user = UserFixture.user();
            given(userRepository.findAll()).willReturn(List.of(user));
            given(userSubscriptionRepository.findAllByUserOrderByCreatedAtDesc(user))
                .willReturn(List.of());

            FindUsersResponse response = adminUserService.findUsers();

            assertThat(response.users()).hasSize(1);
            assertThat(response.users().get(0).subscriptionStatus()).isEqualTo("NO_SUBSCRIPTION");
            assertThat(response.users().get(0).subscriptionEndDate()).isNull();
        }
    }
}

package com.verby.indp.domain.auth.service;

import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.dto.response.UserPaymentsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSubscriptionServiceTest {

    @InjectMocks
    private UserSubscriptionService userSubscriptionService;

    @Nested
    @DisplayName("getSubscription 메서드 실행 시")
    class GetSubscription {

        @Test
        @DisplayName("실패 : 구독 정보가 없으면 NotFoundException을 던진다.")
        void getSubscriptionNotFound() {
            User user = userWithId(1L);
            Exception exception = catchException(
                () -> userSubscriptionService.getSubscription(user));
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getPayments 메서드 실행 시")
    class GetPayments {

        @Test
        @DisplayName("성공 : 빈 결제 내역을 반환한다.")
        void getPayments() {
            User user = userWithId(1L);
            UserPaymentsResponse response = userSubscriptionService.getPayments(user);
            assertThat(response.payments()).isEmpty();
        }
    }
}

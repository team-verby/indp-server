package com.verby.indp.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.dto.request.UserApplicationRequest;
import com.verby.indp.domain.auth.dto.response.UserApplicationResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.ConflictException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import com.verby.indp.global.notification.ApplicationMailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @InjectMocks
    private UserApplicationService userApplicationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserSubscriptionRepository userSubscriptionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationMailService applicationMailService;

    @Nested
    @DisplayName("apply 메서드 실행 시")
    class Apply {

        @Test
        @DisplayName("성공 : Plan A 구독을 신청한다 (월간).")
        void applyMonthly() {
            given(userRepository.existsByLoginId("user123")).willReturn(false);
            given(passwordEncoder.encode("password123!")).willReturn("$2a$hashed");

            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A 라이트 요금제 · 월간", 4400);
            ReflectionTestUtils.setField(payment, "orderId", "test-order-id");
            given(paymentRepository.save(any())).willReturn(payment);
            given(userRepository.save(any())).willReturn(null);
            given(userSubscriptionRepository.save(any())).willReturn(null);

            UserApplicationResponse response = userApplicationService.apply(
                new UserApplicationRequest("user123", "홍길동", "user@test.com", "password123!", 1));

            assertThat(response.amount()).isEqualTo(4400);
            assertThat(response.orderName()).contains("월간");
        }

        @Test
        @DisplayName("성공 : Plan A 구독을 신청한다 (연간).")
        void applyAnnual() {
            given(userRepository.existsByLoginId("user123")).willReturn(false);
            given(passwordEncoder.encode(any())).willReturn("$2a$hashed");

            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A 라이트 요금제 · 연간", 52800);
            ReflectionTestUtils.setField(payment, "orderId", "test-order-id");
            given(paymentRepository.save(any())).willReturn(payment);
            given(userRepository.save(any())).willReturn(null);
            given(userSubscriptionRepository.save(any())).willReturn(null);

            UserApplicationResponse response = userApplicationService.apply(
                new UserApplicationRequest("user123", "홍길동", "user@test.com", "password123!", 12));

            assertThat(response.amount()).isEqualTo(52800);
            assertThat(response.orderName()).contains("연간");
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 아이디이면 예외를 던진다.")
        void applyWithDuplicateLoginId() {
            given(userRepository.existsByLoginId("dupid")).willReturn(true);

            Exception exception = catchException(
                () -> userApplicationService.apply(
                    new UserApplicationRequest("dupid", "홍길동", "user@test.com", "password123!", 1)));

            assertThat(exception).isInstanceOf(ConflictException.class);
        }

        @Test
        @DisplayName("성공 : 이메일이 중복이어도 아이디가 고유하면 신청된다.")
        void applyWithDuplicateEmailAllowed() {
            given(userRepository.existsByLoginId("user123")).willReturn(false);
            given(passwordEncoder.encode(any())).willReturn("$2a$hashed");

            Payment payment = new Payment(PaymentType.USER_SUBSCRIPTION, "Plan A 라이트 요금제 · 월간", 4400);
            ReflectionTestUtils.setField(payment, "orderId", "test-order-id");
            given(paymentRepository.save(any())).willReturn(payment);
            given(userRepository.save(any())).willReturn(null);
            given(userSubscriptionRepository.save(any())).willReturn(null);

            UserApplicationResponse response = userApplicationService.apply(
                new UserApplicationRequest("user123", "홍길동", "dup@test.com", "password123!", 1));

            assertThat(response.amount()).isEqualTo(4400);
        }

        @Test
        @DisplayName("실패 : usagePeriod가 1 또는 12가 아니면 예외를 던진다.")
        void applyWithInvalidUsagePeriod() {
            Exception exception = catchException(
                () -> userApplicationService.apply(
                    new UserApplicationRequest("user123", "홍길동", "user@test.com", "password123!", 3)));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

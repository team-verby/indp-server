package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.dto.reponse.TossPaymentApiResponse;
import com.verby.indp.domain.payment.dto.request.RefundPaymentRequest;
import com.verby.indp.domain.payment.dto.response.FindAdminPaymentsResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import com.verby.indp.fixture.PaymentFixture;
import com.verby.indp.fixture.StoreFixture;
import com.verby.indp.fixture.StoreSubscriptionFixture;
import org.springframework.test.util.ReflectionTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminPaymentServiceTest {

    @InjectMocks
    private AdminPaymentService adminPaymentService;

    @Mock
    private StoreSubscriptionRepository storeSubscriptionRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private PaymentService paymentService;

    @Nested
    @DisplayName("findPayments 메서드 실행 시")
    class FindPayments {

        @Test
        @DisplayName("성공 : 결제 내역 목록을 반환한다.")
        void findPayments() {
            // given
            Store store = StoreFixture.store();
            StoreSubscription subscription = StoreSubscriptionFixture.doneSubscription();
            ReflectionTestUtils.setField(subscription.getPayment(), "paymentId", 1L);
            Page<StoreSubscription> page = new PageImpl<>(List.of(subscription));

            given(storeService.getStoreById(1L)).willReturn(store);
            given(storeSubscriptionRepository.findAllByStoreOrderByPaymentCreatedAtDesc(any(), any()))
                .willReturn(page);

            // when
            FindAdminPaymentsResponse result = adminPaymentService.findPayments(1L, PageRequest.of(0, 20));

            // then
            assertThat(result).isNotNull();
            assertThat(result.payments()).hasSize(1);
            assertThat(result.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공 : 결제 내역이 없으면 빈 목록을 반환한다.")
        void findPaymentsWithEmpty() {
            // given
            Store store = StoreFixture.store();
            Page<StoreSubscription> page = new PageImpl<>(List.of());

            given(storeService.getStoreById(1L)).willReturn(store);
            given(storeSubscriptionRepository.findAllByStoreOrderByPaymentCreatedAtDesc(any(), any()))
                .willReturn(page);

            // when
            FindAdminPaymentsResponse result = adminPaymentService.findPayments(1L, PageRequest.of(0, 20));

            // then
            assertThat(result.payments()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("실패 : 매장이 존재하지 않으면 예외를 던진다.")
        void findPaymentsWithStoreNotFound() {
            // given
            given(storeService.getStoreById(999L)).willThrow(new NotFoundException("매장 정보가 존재하지 않습니다."));

            // when
            Exception exception = catchException(
                () -> adminPaymentService.findPayments(999L, PageRequest.of(0, 20)));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("cancelPayment 메서드 실행 시")
    class CancelPayment {

        @Test
        @DisplayName("성공 : 잔액이 0이면 결제 상태를 CANCELED로 변경한다.")
        void refundPaymentFull() {
            // given
            Payment payment = PaymentFixture.donePayment();
            RefundPaymentRequest request = new RefundPaymentRequest(180000, "단순 변심");
            TossPaymentApiResponse tossResponse = new TossPaymentApiResponse("카드", "CANCELED", 0, 180000);

            given(paymentService.getPaymentById(1L)).willReturn(payment);
            given(paymentClient.cancelPayment(payment.getPaymentKey(), 180000, "단순 변심"))
                .willReturn(tossResponse);

            // when
            adminPaymentService.refundPayment(1L, request);

            // then
            assertThat(payment.isStatusWith(PaymentStatus.CANCELED)).isTrue();
            assertThat(payment.getBalanceAmount()).isZero();
            assertThat(payment.getRefunds()).hasSize(1);
            assertThat(payment.getRefunds().get(0).getCancelAmount()).isEqualTo(180000);
            assertThat(payment.getRefunds().get(0).getCancelReason()).isEqualTo("단순 변심");
        }

        @Test
        @DisplayName("성공 : 잔액이 남으면 결제 상태를 PARTIAL_CANCELED로 변경한다.")
        void refundPaymentPartial() {
            // given
            Payment payment = PaymentFixture.donePayment();
            RefundPaymentRequest request = new RefundPaymentRequest(90000, "부분 환불");
            TossPaymentApiResponse tossResponse = new TossPaymentApiResponse("카드", "PARTIAL_CANCELED", 90000, 180000);

            given(paymentService.getPaymentById(1L)).willReturn(payment);
            given(paymentClient.cancelPayment(payment.getPaymentKey(), 90000, "부분 환불"))
                .willReturn(tossResponse);

            // when
            adminPaymentService.refundPayment(1L, request);

            // then
            assertThat(payment.isStatusWith(PaymentStatus.PARTIAL_CANCELED)).isTrue();
            assertThat(payment.getBalanceAmount()).isEqualTo(90000);
            assertThat(payment.getRefunds()).hasSize(1);
            assertThat(payment.getRefunds().get(0).getCancelAmount()).isEqualTo(90000);
            assertThat(payment.getRefunds().get(0).getCancelReason()).isEqualTo("부분 환불");
        }

        @Test
        @DisplayName("실패 : 환불 요청 금액이 잔액을 초과하면 예외를 던진다.")
        void refundPaymentWithExceedingAmount() {
            // given
            Payment payment = PaymentFixture.donePayment();
            RefundPaymentRequest request = new RefundPaymentRequest(200000, "단순 변심");

            given(paymentService.getPaymentById(1L)).willReturn(payment);

            // when
            Exception exception = catchException(
                () -> adminPaymentService.refundPayment(1L, request));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 결제 정보가 없으면 예외를 던진다.")
        void refundPaymentWithNotFound() {
            // given
            RefundPaymentRequest request = new RefundPaymentRequest(180000, "단순 변심");
            given(paymentService.getPaymentById(999L)).willThrow(new NotFoundException("결제 정보가 존재하지 않습니다."));

            // when
            Exception exception = catchException(
                () -> adminPaymentService.refundPayment(999L, request));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

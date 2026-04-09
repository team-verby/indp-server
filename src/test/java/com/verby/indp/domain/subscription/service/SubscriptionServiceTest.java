package com.verby.indp.domain.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.AddSubscriptionResponse;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.subscription.dto.request.AddSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import com.verby.indp.fixture.OwnerFixture;
import com.verby.indp.fixture.PaymentFixture;
import com.verby.indp.fixture.PlanFixture;
import com.verby.indp.fixture.StoreFixture;
import com.verby.indp.fixture.StoreSubscriptionFixture;
import java.time.LocalDate;
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
class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private StoreService storeService;

    @Mock
    private PlanService planService;

    @Mock
    private StoreSubscriptionRepository storeSubscriptionRepository;

    @Nested
    @DisplayName("orderSubscription 메서드 실행 시")
    class OrderSubscription {

        @Test
        @DisplayName("성공 : 구독을 주문한다.")
        void orderSubscription() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Plan plan = PlanFixture.planA();
            Store store = StoreFixture.storeWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);
            given(planService.getPlan(1L)).willReturn(plan);

            AddSubscriptionRequest request = new AddSubscriptionRequest(1L, 3);

            AddSubscriptionResponse result = subscriptionService.orderSubscription(owner, 1L,
                request);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void orderSubscriptionWithNotOwned() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store store = StoreFixture.storeWithOwner(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            AddSubscriptionRequest request = new AddSubscriptionRequest(1L, 3);

            Exception exception = catchException(
                () -> subscriptionService.orderSubscription(owner, 1L, request));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findSubscriptions 메서드 실행 시")
    class FindSubscriptions {

        @Test
        @DisplayName("성공 : 구독 목록을 반환한다.")
        void findSubscriptions() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.storeWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);
            given(storeSubscriptionRepository.findAllByStore(store)).willReturn(List.of());

            FindSubscriptionsResponse expected = FindSubscriptionsResponse.from(List.of());

            // when
            FindSubscriptionsResponse result = subscriptionService.findSubscriptions(owner, 1L);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void findSubscriptionsWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store store = StoreFixture.storeWithOwner(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(
                () -> subscriptionService.findSubscriptions(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("activateSubscriptions 메서드 실행 시")
    class ActivateSubscriptions {

        @Test
        @DisplayName("성공 : 활성화할 구독이 없으면 아무것도 하지 않는다.")
        void activateSubscriptionsWithEmpty() {
            given(storeSubscriptionRepository.findAllByStatusAndStartDateLessThanEqual(
                SubscriptionStatus.PENDING_ACTIVE, LocalDate.now()))
                .willReturn(List.of());

            Exception exception = catchException(() -> subscriptionService.activateSubscriptions());

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 구독을 활성화한다.")
        void activateSubscriptions() {
            StoreSubscription subscription = StoreSubscriptionFixture.pendingActiveSubscription();
            given(storeSubscriptionRepository.findAllByStatusAndStartDateLessThanEqual(
                any(), any()))
                .willReturn(List.of(subscription));

            subscriptionService.activateSubscriptions();

            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("expireSubscriptions 메서드 실행 시")
    class ExpireSubscriptions {

        @Test
        @DisplayName("성공 : 만료할 구독이 없으면 아무것도 하지 않는다.")
        void expireSubscriptionsWithEmpty() {
            given(storeSubscriptionRepository.findAllByStatusAndEndDateBefore(
                SubscriptionStatus.ACTIVE, LocalDate.now()))
                .willReturn(List.of());

            Exception exception = catchException(() -> subscriptionService.expireSubscriptions());

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 구독을 만료한다.")
        void expireSubscriptions() {
            StoreSubscription subscription = StoreSubscriptionFixture.activeSubscription();
            given(storeSubscriptionRepository.findAllByStatusAndEndDateBefore(any(), any()))
                .willReturn(List.of(subscription));

            subscriptionService.expireSubscriptions();

            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.EXPIRED);
        }
    }

    @Nested
    @DisplayName("confirmPayment 메서드 실행 시")
    class ConfirmPayment {

        @Test
        @DisplayName("성공 : 결제를 확인하고 구독을 활성화 대기 상태로 변경한다.")
        void confirmPayment() {
            Payment payment = PaymentFixture.payment();
            StoreSubscription subscription = StoreSubscriptionFixture.pendingPaymentSubscription();
            given(storeSubscriptionRepository.findByPayment(payment))
                .willReturn(Optional.of(subscription));

            subscriptionService.confirmPayment(payment);

            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.PENDING_ACTIVE);
        }

        @Test
        @DisplayName("실패 : 결제에 대한 구독이 없으면 예외를 던진다.")
        void confirmPaymentWithNotFound() {
            Payment payment = PaymentFixture.payment();
            given(storeSubscriptionRepository.findByPayment(payment))
                .willReturn(Optional.empty());

            Exception exception = catchException(() -> subscriptionService.confirmPayment(payment));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

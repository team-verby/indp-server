package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.dto.response.FindOwnerPaymentsResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import com.verby.indp.fixture.OwnerFixture;
import com.verby.indp.fixture.StoreFixture;
import com.verby.indp.fixture.StoreSubscriptionFixture;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OwnerPaymentServiceTest {

    @InjectMocks
    private OwnerPaymentService ownerPaymentService;

    @Mock
    private StoreSubscriptionRepository storeSubscriptionRepository;

    @Mock
    private StoreService storeService;

    @Nested
    @DisplayName("findPayments 메서드 실행 시")
    class FindPayments {

        @Test
        @DisplayName("성공 : 결제 내역 목록을 반환한다.")
        void findPayments() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.storeWithOwner(owner);
            StoreSubscription subscription = StoreSubscriptionFixture.doneSubscription();
            Page<StoreSubscription> page = new PageImpl<>(List.of(subscription));

            given(storeService.getStoreById(1L)).willReturn(store);
            given(storeSubscriptionRepository.findAllByStoreOrderByPaymentCreatedAtDesc(
                eq(store), any()))
                .willReturn(page);

            // when
            FindOwnerPaymentsResponse result = ownerPaymentService.findPayments(owner, 1L,
                PageRequest.of(0, 10));

            // then
            assertThat(result).isNotNull();
            assertThat(result.payments()).hasSize(1);
            assertThat(result.totalElements()).isEqualTo(1);
            assertThat(result.totalPages()).isEqualTo(1);
        }

        @Test
        @DisplayName("성공 : 결제 내역이 없으면 빈 목록을 반환한다.")
        void findPaymentsWithEmpty() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.storeWithOwner(owner);
            Page<StoreSubscription> page = new PageImpl<>(List.of());

            given(storeService.getStoreById(1L)).willReturn(store);
            given(storeSubscriptionRepository.findAllByStoreOrderByPaymentCreatedAtDesc(
                eq(store), any()))
                .willReturn(page);

            // when
            FindOwnerPaymentsResponse result = ownerPaymentService.findPayments(owner, 1L,
                PageRequest.of(0, 10));

            // then
            assertThat(result.payments()).isEmpty();
            assertThat(result.totalElements()).isZero();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void findPaymentsWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store store = StoreFixture.storeWithOwner(otherOwner);

            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(
                () -> ownerPaymentService.findPayments(owner, 1L, PageRequest.of(0, 10)));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

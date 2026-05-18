package com.verby.indp.domain.store.service;

import static com.verby.indp.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindStoreSummaryResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("findStores 메서드 실행 시")
    class FindStores {

        @Test
        @DisplayName("성공 : 활성 구독 매장 목록을 반환한다.")
        void findStores() {
            // given
            Page<Store> page = new PageImpl<>(List.of());
            given(storeRepository.findAllBySubscriptionStatus(any(SubscriptionStatus.class), any()))
                .willReturn(page);

            // when
            FindStoresResponse result = storeService.findStores(PageRequest.of(0, 10));

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("findStoreSummary 메서드 실행 시")
    class FindStoreSummary {

        @Test
        @DisplayName("성공 : 매장 요약 정보를 반환한다.")
        void findStoreSummary() {
            // given
            Store mockStore = store();
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            FindStoreSummaryResponse result = storeService.findStoreSummary(1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void findStoreSummaryWithNotExist() {
            // given
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> storeService.findStoreSummary(999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getStoreById 메서드 실행 시")
    class GetStoreById {

        @Test
        @DisplayName("성공 : 매장을 반환한다.")
        void getStoreById() {
            // given
            Store store = store();
            given(storeRepository.findById(1L)).willReturn(Optional.of(store));

            // when
            Store result = storeService.getStoreById(1L);

            // then
            assertThat(result).isEqualTo(store);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void getStoreByIdWithNotExist() {
            // given
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> storeService.getStoreById(999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getStoreByName 메서드 실행 시")
    class GetStoreByName {

        @Test
        @DisplayName("성공 : 매장명으로 매장을 반환한다.")
        void getStoreByName() {
            // given
            Store store = store();
            given(storeRepository.findByName("카페공명")).willReturn(Optional.of(store));

            // when
            Store result = storeService.getStoreByName("카페공명");

            // then
            assertThat(result).isEqualTo(store);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장명이면 예외를 던진다.")
        void getStoreByNameWithNotExist() {
            // given
            given(storeRepository.findByName("없는매장")).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> storeService.getStoreByName("없는매장"));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

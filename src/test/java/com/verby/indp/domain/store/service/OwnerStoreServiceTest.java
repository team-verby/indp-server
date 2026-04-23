package com.verby.indp.domain.store.service;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.MusicGenre;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindLatestSubscriptionResponse;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.GenreItem;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.vo.Genre;
import com.verby.indp.domain.store.dto.response.FindStoreByOwnerResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByOwnerResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.store.vo.PlaylistType;
import com.verby.indp.domain.store.vo.Tempo;
import com.verby.indp.fixture.OwnerFixture;
import com.verby.indp.fixture.StoreFixture;
import java.time.LocalTime;
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
class OwnerStoreServiceTest {

    @InjectMocks
    private OwnerStoreService ownerStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("getMyStores 메서드 실행 시")
    class GetMyStores {

        @Test
        @DisplayName("성공 : 점주의 매장 목록을 반환한다.")
        void getMyStores() {
            // given
            Owner owner = owner();
            given(storeRepository.findAllByOwner(owner)).willReturn(List.of());

            // when
            FindStoresByOwnerResponse result = ownerStoreService.getMyStores(owner);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("getMyStore 메서드 실행 시")
    class GetMyStore {

        @Test
        @DisplayName("성공 : 점주의 매장 상세를 반환한다.")
        void getMyStore() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            FindStoreByOwnerResponse result = ownerStoreService.getMyStore(owner, 1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void getMyStoreWithNotExist() {
            // given
            Owner owner = owner();
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> ownerStoreService.getMyStore(owner, 999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getMyStoreWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            Exception exception = catchException(() -> ownerStoreService.getMyStore(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateStore 메서드 실행 시")
    class UpdateStore {

        @Test
        @DisplayName("성공 : 매장 정보를 수정한다.")
        void updateStore() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateStoreRequest request = new UpdateStoreRequest(
                "카페 공명", "카페", "서울",
                List.of(new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false)),
                List.of(), "20대", 3, "유튜브", "인디",
                List.of(PlayMethod.Method.BLUETOOTH), List.of(),
                PlaylistType.CONSISTENT_MOOD, List.of(), Tempo.CALM,
                List.of(new GenreItem(Genre.INDIE, MusicGenre.PreferenceType.LIKE)), "", "아늑한");

            // when
            Exception exception = catchException(
                () -> ownerStoreService.updateStore(owner, 1L, request));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void updateStoreWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateStoreRequest request = new UpdateStoreRequest(
                "카페 공명", "카페", "서울", List.of(), List.of(), "20대", 3, "유튜브", "인디",
                List.of(), List.of(), PlaylistType.CONSISTENT_MOOD, List.of(), Tempo.CALM,
                List.of(), "", "아늑한");

            // when
            Exception exception = catchException(
                () -> ownerStoreService.updateStore(owner, 1L, request));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getLatestSubscription 메서드 실행 시")
    class GetLatestSubscription {

        @Test
        @DisplayName("성공 : 최신 구독 정보를 반환한다.")
        void getLatestSubscription() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            FindLatestSubscriptionResponse result = ownerStoreService.getLatestSubscription(owner,
                1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getLatestSubscriptionWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            Exception exception = catchException(
                () -> ownerStoreService.getLatestSubscription(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void getLatestSubscriptionWithNotExist() {
            // given
            Owner owner = owner();
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> ownerStoreService.getLatestSubscription(owner, 999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

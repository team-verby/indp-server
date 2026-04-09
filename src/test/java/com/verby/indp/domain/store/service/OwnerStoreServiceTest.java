package com.verby.indp.domain.store.service;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.PlayMethod;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindLatestSubscriptionResponse;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.dto.response.FindOwnerStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
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
import org.mockito.Mockito;
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
            Owner owner = owner();
            given(storeRepository.findAllByOwner(owner)).willReturn(List.of());

            FindStoresResponse result = ownerStoreService.getMyStores(owner);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("getMyStore 메서드 실행 시")
    class GetMyStore {

        @Test
        @DisplayName("성공 : 점주의 매장 상세를 반환한다.")
        void getMyStore() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            FindOwnerStoreResponse result = ownerStoreService.getMyStore(owner, 1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void getMyStoreWithNotExist() {
            Owner owner = owner();
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            Exception exception = catchException(() -> ownerStoreService.getMyStore(owner, 999L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getMyStoreWithNotOwned() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            Exception exception = catchException(() -> ownerStoreService.getMyStore(owner, 1L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateStore 메서드 실행 시")
    class UpdateStore {

        @Test
        @DisplayName("성공 : 매장 정보를 수정한다.")
        void updateStore() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateStoreRequest request = Mockito.mock(UpdateStoreRequest.class);
            given(request.platform()).willReturn("유튜브");
            given(request.playedMusic()).willReturn("인디");
            given(request.playlistType()).willReturn(PlaylistType.CONSISTENT_MOOD);
            given(request.musicTempo()).willReturn(Tempo.CALM);
            given(request.playMethods()).willReturn(List.of(PlayMethod.Method.BLUETOOTH));
            given(request.timePreferences()).willReturn(List.of());
            given(request.preferenceGenres()).willReturn(List.of());
            BusinessHour bh = new BusinessHour(1, LocalTime.of(10, 0), LocalTime.of(22, 0), false);
            given(request.businessHours()).willReturn(List.of(bh));
            given(request.rejectedSongNote()).willReturn("");
            given(request.mood()).willReturn("아늑한");
            given(request.name()).willReturn("카페 공명");
            given(request.industry()).willReturn("카페");
            given(request.address()).willReturn("서울");
            given(request.customerAgeGroup()).willReturn("20대");
            given(request.lighting()).willReturn(3);
            given(request.vibes()).willReturn(List.of());
            given(request.photoUrls()).willReturn(List.of());

            Exception exception = catchException(
                () -> ownerStoreService.updateStore(owner, 1L, request));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void updateStoreWithNotOwned() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateStoreRequest request = Mockito.mock(UpdateStoreRequest.class);

            Exception exception = catchException(
                () -> ownerStoreService.updateStore(owner, 1L, request));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getLatestSubscription 메서드 실행 시")
    class GetLatestSubscription {

        @Test
        @DisplayName("성공 : 최신 구독 정보를 반환한다.")
        void getLatestSubscription() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store mockStore = StoreFixture.storeWithOwner(owner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            FindLatestSubscriptionResponse result = ownerStoreService.getLatestSubscription(owner,
                1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getLatestSubscriptionWithNotOwned() {
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store mockStore = StoreFixture.storeWithOwner(otherOwner);
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            Exception exception = catchException(
                () -> ownerStoreService.getLatestSubscription(owner, 1L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void getLatestSubscriptionWithNotExist() {
            Owner owner = owner();
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            Exception exception = catchException(
                () -> ownerStoreService.getLatestSubscription(owner, 999L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

package com.verby.indp.domain.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.slack.SlackNotificationService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OwnerPlaylistServiceTest {

    @InjectMocks
    private OwnerPlaylistService ownerPlaylistService;

    @Mock
    private StoreService storeService;

    @Mock
    private SlackNotificationService slackNotificationService;

    @Mock
    private PlaylistService playlistService;

    @Nested
    @DisplayName("getStorePlaylist 메서드 실행 시")
    class GetStorePlaylist {

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 null을 반환한다.")
        void getStorePlaylistWithNullPlaylist() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner storeOwner = Mockito.mock(Owner.class);
            given(storeOwner.getOwnerId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(storeOwner);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(null);
            given(storeService.getStoreById(1L)).willReturn(store);

            FindStorePlaylistByOwnerResponse result = ownerPlaylistService.getStorePlaylist(owner, 1L);

            assertThat(result.playlist()).isNull();
            assertThat(result.currentSong()).isNull();
        }

        @Test
        @DisplayName("성공 : 플레이리스트를 반환한다.")
        void getStorePlaylist() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner storeOwner = Mockito.mock(Owner.class);
            given(storeOwner.getOwnerId()).willReturn(1L);

            Playlist playlist = Mockito.mock(Playlist.class);
            given(playlist.getPlaylistId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(storeOwner);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(playlist);
            given(storeService.getStoreById(1L)).willReturn(store);
            given(playlistService.getSortedSongs(1L)).willReturn(List.of());

            FindStorePlaylistByOwnerResponse result = ownerPlaylistService.getStorePlaylist(owner, 1L);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithNotOwned() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner otherOwner = Mockito.mock(Owner.class);
            given(otherOwner.getOwnerId()).willReturn(2L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() -> ownerPlaylistService.getStorePlaylist(owner, 1L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithInactiveSubscription() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner storeOwner = Mockito.mock(Owner.class);
            given(storeOwner.getOwnerId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(storeOwner);
            given(store.isInactive()).willReturn(true);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() -> ownerPlaylistService.getStorePlaylist(owner, 1L));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("regeneratePlaylist 메서드 실행 시")
    class RegeneratePlaylist {

        @Test
        @DisplayName("성공 : 슬랙 알림을 전송한다.")
        void regeneratePlaylist() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner storeOwner = Mockito.mock(Owner.class);
            given(storeOwner.getOwnerId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(storeOwner);
            given(store.isInactive()).willReturn(false);
            given(storeService.getStoreById(1L)).willReturn(store);

            ownerPlaylistService.regeneratePlaylist(owner, 1L);

            then(slackNotificationService).should().handlePlaylistRegenerationRequest(store);
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void regeneratePlaylistWithNotOwned() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner otherOwner = Mockito.mock(Owner.class);
            given(otherOwner.getOwnerId()).willReturn(2L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() -> ownerPlaylistService.regeneratePlaylist(owner, 1L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void regeneratePlaylistWithInactiveSubscription() {
            Owner owner = Mockito.mock(Owner.class);
            given(owner.getOwnerId()).willReturn(1L);

            Owner storeOwner = Mockito.mock(Owner.class);
            given(storeOwner.getOwnerId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.getOwner()).willReturn(storeOwner);
            given(store.isInactive()).willReturn(true);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() -> ownerPlaylistService.regeneratePlaylist(owner, 1L));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

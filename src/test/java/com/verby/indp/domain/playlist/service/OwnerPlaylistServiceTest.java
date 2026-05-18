package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.ForbiddenException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.fixture.OwnerFixture;
import com.verby.indp.fixture.PlaylistFixture;
import com.verby.indp.fixture.StoreFixture;
import com.verby.indp.global.slack.SlackNotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

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

    @Mock
    private CurrentSongResolver currentSongResolver;

    @Nested
    @DisplayName("getStorePlaylist 메서드 실행 시")
    class GetStorePlaylist {

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 null을 반환한다.")
        void getStorePlaylistWithNullPlaylist() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.storeWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            FindStorePlaylistByOwnerResponse result = ownerPlaylistService.getStorePlaylist(owner, 1L);

            // then
            assertThat(result.playlist()).isNull();
            assertThat(result.currentSong()).isNull();
        }

        @Test
        @DisplayName("성공 : 플레이리스트를 반환한다.")
        void getStorePlaylist() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Playlist playlist = PlaylistFixture.playlist();
            Store store = StoreFixture.storeWithOwner(owner);
            store.assignPlaylist(playlist);
            given(storeService.getStoreById(1L)).willReturn(store);
            given(playlistService.getSortedSongs(1L)).willReturn(List.of());

            // when
            FindStorePlaylistByOwnerResponse result = ownerPlaylistService.getStorePlaylist(owner, 1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store store = StoreFixture.storeWithOwner(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() -> ownerPlaylistService.getStorePlaylist(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithInactiveSubscription() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.inactiveStoreWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() -> ownerPlaylistService.getStorePlaylist(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("regeneratePlaylist 메서드 실행 시")
    class RegeneratePlaylist {

        @Test
        @DisplayName("성공 : 슬랙 알림을 전송한다.")
        void regeneratePlaylist() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.storeWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            ownerPlaylistService.regeneratePlaylist(owner, 1L);

            // then
            then(slackNotificationService).should().handlePlaylistRegenerationRequest(store);
        }

        @Test
        @DisplayName("실패 : 소유하지 않은 매장이면 예외를 던진다.")
        void regeneratePlaylistWithNotOwned() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Owner otherOwner = OwnerFixture.ownerWithId(2L);
            Store store = StoreFixture.storeWithOwner(otherOwner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() -> ownerPlaylistService.regeneratePlaylist(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void regeneratePlaylistWithInactiveSubscription() {
            // given
            Owner owner = OwnerFixture.ownerWithId(1L);
            Store store = StoreFixture.inactiveStoreWithOwner(owner);
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() -> ownerPlaylistService.regeneratePlaylist(owner, 1L));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

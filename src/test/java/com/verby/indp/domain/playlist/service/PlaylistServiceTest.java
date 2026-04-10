package com.verby.indp.domain.playlist.service;

import static com.verby.indp.fixture.PlaylistFixture.playlist;
import static com.verby.indp.fixture.PlaylistFixture.playlistWithSongs;
import static com.verby.indp.fixture.PlaylistSongFixture.playlistSong;
import static com.verby.indp.fixture.PlaylistSongFixture.playlistSongWithId;
import static com.verby.indp.fixture.PlaylistSongFixture.recommendedSong;
import static com.verby.indp.fixture.ScheduledPlaylistFixture.scheduledPlaylistWithSongs;
import static com.verby.indp.fixture.SongRecommendationFixture.songRecommendation;
import static com.verby.indp.fixture.StoreFixture.inactiveStore;
import static com.verby.indp.fixture.StoreFixture.store;
import static com.verby.indp.fixture.StoreFixture.storeWithPlaylist;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistSongRepository playlistSongRepository;

    @Mock
    private ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Mock
    private StoreService storeService;

    @Nested
    @DisplayName("getStorePlaylist 메서드 실행 시")
    class GetStorePlaylist {

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 null을 반환한다.")
        void getStorePlaylistWithNullPlaylist() {
            // given
            Store store = store();
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            FindStorePlaylistResponse result = playlistService.getStorePlaylist(1L);

            // then
            assertThat(result.playlist()).isNull();
            assertThat(result.currentSong()).isNull();
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithInactiveSubscription() {
            // given
            Store store = inactiveStore();
            given(storeService.getStoreById(1L)).willReturn(store);

            // when
            Exception exception = catchException(() -> playlistService.getStorePlaylist(1L));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 플레이리스트가 있으면 응답을 반환한다.")
        void getStorePlaylistWithPlaylist() {
            // given
            Playlist pl = playlist();
            Store store = storeWithPlaylist(pl);
            given(storeService.getStoreById(1L)).willReturn(store);
            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of());

            // when
            FindStorePlaylistResponse result = playlistService.getStorePlaylist(1L);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("applyDueScheduledUpdates 메서드 실행 시")
    class ApplyDueScheduledUpdates {

        @Test
        @DisplayName("성공 : 스케줄된 업데이트가 없으면 아무것도 하지 않는다.")
        void applyDueScheduledUpdatesWithEmpty() {
            // given
            given(scheduledPlaylistUpdateRepository.findAllByStatusAndScheduledAtLessThanEqual(
                any(), any()))
                .willReturn(List.of());

            // when
            Exception exception = catchException(() -> playlistService.applyDueScheduledUpdates());

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 스케줄된 업데이트가 있으면 플레이리스트를 업데이트한다.")
        void applyDueScheduledUpdatesWithScheduledPlaylist() {
            // given
            ScheduledPlaylistSong scheduledSong = new ScheduledPlaylistSong(
                "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, 1.0);
            ScheduledPlaylist scheduledPlaylist = scheduledPlaylistWithSongs(List.of(scheduledSong));

            given(scheduledPlaylistUpdateRepository.findAllByStatusAndScheduledAtLessThanEqual(
                any(), any()))
                .willReturn(List.of(scheduledPlaylist));

            // when
            Exception exception = catchException(() -> playlistService.applyDueScheduledUpdates());

            // then
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("deleteRecommendedSongs 메서드 실행 시")
    class DeleteRecommendedSongs {

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 아무것도 하지 않는다.")
        void deleteRecommendedSongsWithNullPlaylist() {
            // given
            Store store = store();

            // when
            Exception exception = catchException(
                () -> playlistService.deleteRecommendedSongs(store));

            // then
            assertThat(exception).isNull();
            then(playlistSongRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("성공 : 추천 곡을 삭제한다.")
        void deleteRecommendedSongs() {
            // given
            PlaylistSong recommendedSong = recommendedSong();
            PlaylistSong normalSong = playlistSong();
            Playlist playlist = playlistWithSongs(new ArrayList<>(List.of(recommendedSong, normalSong)));
            Store store = storeWithPlaylist(playlist);

            willDoNothing().given(playlistSongRepository).deleteAll(any());

            // when
            playlistService.deleteRecommendedSongs(store);

            // then
            then(playlistSongRepository).should().deleteAll(List.of(recommendedSong));
        }
    }

    @Nested
    @DisplayName("addRecommendedSong 메서드 실행 시")
    class AddRecommendedSong {

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void addRecommendedSongWithInactiveStore() {
            // given
            Store store = inactiveStore();
            SongRecommendation recommendation = songRecommendation();

            // when
            Exception exception = catchException(
                () -> playlistService.addRecommendedSong(store, recommendation));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 플레이리스트가 없는 매장이면 예외를 던진다.")
        void addRecommendedSongWithNullPlaylist() {
            // given
            Store store = store();
            SongRecommendation recommendation = songRecommendation();

            // when
            Exception exception = catchException(
                () -> playlistService.addRecommendedSong(store, recommendation));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("성공 : 추천 곡을 플레이리스트에 추가한다.")
        void addRecommendedSongSuccess() {
            // given
            SongRecommendation recommendation = songRecommendation();
            PlaylistSong currentSong = playlistSongWithId(42L, 10.0, 86400);
            Playlist playlist = playlistWithSongs(List.of(currentSong));
            PlaylistSong repoSong = playlistSongWithId(42L, 10.0);
            Store store = storeWithPlaylist(playlist);

            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(repoSong));

            // when
            PlaylistSong result = playlistService.addRecommendedSong(store, recommendation);

            // then
            assertThat(result).isNotNull();
            assertThat(result.isRecommended()).isTrue();
        }

        @Test
        @DisplayName("성공 : 노래가 6곡 이상일 때 삽입 위치 계산이 성공한다.")
        void addRecommendedSongWithManySongs() {
            SongRecommendation recommendation = songRecommendation();
            // given
            PlaylistSong resolverSong = playlistSongWithId(1L, 10.0, 86400);
            Playlist playlist = playlistWithSongs(List.of(resolverSong));

            PlaylistSong song1 = playlistSongWithId(1L, 10.0);
            PlaylistSong song2 = playlistSongWithId(2L, 20.0);
            PlaylistSong song3 = playlistSongWithId(3L, 30.0);
            PlaylistSong song4 = playlistSongWithId(4L, 40.0);
            PlaylistSong song5 = playlistSongWithId(5L, 50.0);
            PlaylistSong song6 = playlistSongWithId(6L, 60.0);

            Store store = storeWithPlaylist(playlist);

            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(song1, song2, song3, song4, song5, song6));

            // when
            PlaylistSong result = playlistService.addRecommendedSong(store, recommendation);

            // when
            assertThat(result).isNotNull();
        }
    }
}

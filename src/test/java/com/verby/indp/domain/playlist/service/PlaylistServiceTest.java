package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.repository.StoreBusinessHourRepository;
import com.verby.indp.domain.store.service.StoreService;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.lenient;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.verby.indp.fixture.PlaylistFixture.playlist;
import static com.verby.indp.fixture.PlaylistFixture.playlistWithSongs;
import static com.verby.indp.fixture.PlaylistSongFixture.*;
import static com.verby.indp.fixture.ScheduledPlaylistFixture.scheduledPlaylistWithSongs;
import static com.verby.indp.fixture.SongRecommendationFixture.songRecommendation;
import static com.verby.indp.fixture.StoreFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @InjectMocks
    private PlaylistService playlistService;

    @Mock
    private PlaylistSongRepository playlistSongRepository;

    @Mock
    private ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Mock
    private StoreBusinessHourRepository storeBusinessHourRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private CurrentSongResolver currentSongResolver;

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        lenient().when(clock.instant()).thenReturn(Instant.parse("2026-04-24T03:00:00Z"));
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

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
    @DisplayName("deleteRecommendedSongsOfClosingStores 메서드 실행 시")
    class DeleteRecommendedSongsOfClosingStores {

        @Test
        @DisplayName("성공 : 마감 매장이 없으면 아무것도 하지 않는다.")
        void deleteRecommendedSongsWithNoClosingStores() {
            // given
            given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(any(Integer.class), any(), any()))
                .willReturn(List.of());

            // when
            Exception exception = catchException(
                () -> playlistService.deleteRecommendedSongsOfClosingStores(1, LocalTime.of(21, 30), LocalTime.of(22, 0)));

            // then
            assertThat(exception).isNull();
            then(playlistSongRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 아무것도 하지 않는다.")
        void deleteRecommendedSongsWithNullPlaylist() {
            // given
            Store store = store();
            StoreBusinessHour businessHour = new StoreBusinessHour(store, 1, LocalTime.of(9, 0), LocalTime.of(22, 0), false);
            given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(any(Integer.class), any(), any()))
                .willReturn(List.of(businessHour));

            // when
            Exception exception = catchException(
                () -> playlistService.deleteRecommendedSongsOfClosingStores(1, LocalTime.of(21, 30), LocalTime.of(22, 0)));

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
            StoreBusinessHour businessHour = new StoreBusinessHour(store, 1, LocalTime.of(9, 0), LocalTime.of(22, 0), false);

            given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(any(Integer.class), any(), any()))
                .willReturn(List.of(businessHour));
            willDoNothing().given(playlistSongRepository).deleteAll(any());

            // when
            playlistService.deleteRecommendedSongsOfClosingStores(1, LocalTime.of(21, 30), LocalTime.of(22, 0));

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
            given(currentSongResolver.resolveCurrentSong(store))
                .willReturn(Optional.of(new CurrentSong(42L, "title", "artist", "vid", 0)));

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
            given(currentSongResolver.resolveCurrentSong(store))
                .willReturn(Optional.of(new CurrentSong(1, "title", "artist", "vid", 0)));

            // when
            PlaylistSong result = playlistService.addRecommendedSong(store, recommendation);

            // when
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("성공 : 두 번 추천 시 먼저 추천한 곡이 앞 순서에 위치한다 (FIFO)")
        void addRecommendedSongTwiceMaintainsFifoOrder() {
            // given
            SongRecommendation recommendation1 = songRecommendation();
            SongRecommendation recommendation2 = songRecommendation();

            PlaylistSong resolverSong = playlistSongWithId(1L, 10.0, 86400);
            Playlist playlist = playlistWithSongs(List.of(resolverSong));
            Store store = storeWithPlaylist(playlist);

            PlaylistSong song1 = playlistSongWithId(1L, 10.0);
            PlaylistSong song2 = playlistSongWithId(2L, 20.0);
            PlaylistSong song3 = playlistSongWithId(3L, 30.0);
            PlaylistSong song4 = playlistSongWithId(4L, 40.0);
            PlaylistSong song5 = playlistSongWithId(5L, 50.0);
            PlaylistSong song6 = playlistSongWithId(6L, 60.0);
            PlaylistSong song7 = playlistSongWithId(7L, 70.0);

            given(currentSongResolver.resolveCurrentSong(store))
                .willReturn(Optional.of(new CurrentSong(1L, "title", "artist", "vid", 0)));

            // 첫 번째 추천: 추천곡 없음
            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(song1, song2, song3, song4, song5, song6, song7));

            // when
            PlaylistSong result1 = playlistService.addRecommendedSong(store, recommendation1);

            // 두 번째 추천: 첫 번째 추천곡이 포함된 상태
            PlaylistSong recommended1 = recommendedSongWithId(100L, result1.getPlayOrder());
            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(song1, song2, song3, song4, song5, recommended1, song6, song7));

            PlaylistSong result2 = playlistService.addRecommendedSong(store, recommendation2);

            // then
            assertThat(result1.getPlayOrder()).isLessThan(result2.getPlayOrder());
            assertThat(result2.getPlayOrder()).isLessThan(60.0);
        }

        @Test
        @DisplayName("성공 : 다섯 번 추천 시 추천 순서대로 playOrder가 증가한다 (FIFO)")
        void addRecommendedSongFiveTimesMaintainsFifoOrder() {
            // given
            Playlist playlist = playlistWithSongs(List.of(playlistSongWithId(1L, 10.0, 86400)));
            Store store = storeWithPlaylist(playlist);

            PlaylistSong song1 = playlistSongWithId(1L, 10.0);
            PlaylistSong song2 = playlistSongWithId(2L, 20.0);
            PlaylistSong song3 = playlistSongWithId(3L, 30.0);
            PlaylistSong song4 = playlistSongWithId(4L, 40.0);
            PlaylistSong song5 = playlistSongWithId(5L, 50.0);
            PlaylistSong song6 = playlistSongWithId(6L, 60.0);
            PlaylistSong song7 = playlistSongWithId(7L, 70.0);
            PlaylistSong song8 = playlistSongWithId(8L, 80.0);

            given(currentSongResolver.resolveCurrentSong(store))
                .willReturn(Optional.of(new CurrentSong(1L, "title", "artist", "vid", 0)));

            List<PlaylistSong> currentSongs = new ArrayList<>(
                List.of(song1, song2, song3, song4, song5, song6, song7, song8));

            double[] orders = new double[5];
            for (int i = 0; i < 5; i++) {
                given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                    .willReturn(new ArrayList<>(currentSongs));

                // when
                PlaylistSong result = playlistService.addRecommendedSong(store, songRecommendation());
                orders[i] = result.getPlayOrder();

                // 다음 호출을 위해 추천곡을 리스트에 삽입
                PlaylistSong inserted = recommendedSongWithId(100L + i, result.getPlayOrder());
                currentSongs.add(inserted);
                currentSongs.sort((a, b) -> Double.compare(a.getPlayOrder(), b.getPlayOrder()));
            }

            // then: 추천 순서대로 playOrder가 증가해야 한다
            for (int i = 0; i < 4; i++) {
                assertThat(orders[i]).isLessThan(orders[i + 1]);
            }
            // 모든 추천곡이 song5(50)과 song6(60) 사이에 있어야 한다
            for (double order : orders) {
                assertThat(order).isGreaterThan(50.0);
                assertThat(order).isLessThan(60.0);
            }
        }
    }
}

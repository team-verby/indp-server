package com.verby.indp.domain.playlist.service;

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
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.service.StoreService;
import java.time.LocalDate;
import java.time.LocalTime;
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
import org.mockito.Mockito;
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
            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(null);
            given(storeService.getStoreById(1L)).willReturn(store);

            FindStorePlaylistResponse result = playlistService.getStorePlaylist(1L);

            assertThat(result.playlist()).isNull();
            assertThat(result.currentSong()).isNull();
        }

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void getStorePlaylistWithInactiveSubscription() {
            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(true);
            given(storeService.getStoreById(1L)).willReturn(store);

            Exception exception = catchException(() -> playlistService.getStorePlaylist(1L));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 플레이리스트가 있으면 응답을 반환한다.")
        void getStorePlaylistWithPlaylist() {
            Playlist playlist = Mockito.mock(Playlist.class);
            given(playlist.getPlaylistId()).willReturn(1L);

            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(playlist);
            given(store.getBusinessHours()).willReturn(List.of());
            given(storeService.getStoreById(1L)).willReturn(store);
            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of());

            FindStorePlaylistResponse result = playlistService.getStorePlaylist(1L);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("applyDueScheduledUpdates 메서드 실행 시")
    class ApplyDueScheduledUpdates {

        @Test
        @DisplayName("성공 : 스케줄된 업데이트가 없으면 아무것도 하지 않는다.")
        void applyDueScheduledUpdatesWithEmpty() {
            given(scheduledPlaylistUpdateRepository.findAllByStatusAndScheduledAtLessThanEqual(
                any(), any()))
                .willReturn(List.of());

            Exception exception = catchException(() -> playlistService.applyDueScheduledUpdates());

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 스케줄된 업데이트가 있으면 플레이리스트를 업데이트한다.")
        void applyDueScheduledUpdatesWithScheduledPlaylist() {
            ScheduledPlaylistSong scheduledSong = Mockito.mock(ScheduledPlaylistSong.class);
            given(scheduledSong.getVid()).willReturn("5zAEiu3SaO4");
            given(scheduledSong.getPlayTime()).willReturn(259);
            given(scheduledSong.getTitle()).willReturn("안녕 나의 사랑");
            given(scheduledSong.getArtist()).willReturn("성시경");

            Store store = Mockito.mock(Store.class);

            ScheduledPlaylist scheduledPlaylist = Mockito.mock(ScheduledPlaylist.class);
            given(scheduledPlaylist.getStore()).willReturn(store);
            given(scheduledPlaylist.getSongs()).willReturn(List.of(scheduledSong));

            given(scheduledPlaylistUpdateRepository.findAllByStatusAndScheduledAtLessThanEqual(
                any(), any()))
                .willReturn(List.of(scheduledPlaylist));

            Exception exception = catchException(() -> playlistService.applyDueScheduledUpdates());

            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("deleteRecommendedSongs 메서드 실행 시")
    class DeleteRecommendedSongs {

        @Test
        @DisplayName("성공 : 플레이리스트가 없으면 아무것도 하지 않는다.")
        void deleteRecommendedSongsWithNullPlaylist() {
            Store store = Mockito.mock(Store.class);
            given(store.getPlaylist()).willReturn(null);

            Exception exception = catchException(
                () -> playlistService.deleteRecommendedSongs(store));

            assertThat(exception).isNull();
            then(playlistSongRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("성공 : 추천 곡을 삭제한다.")
        void deleteRecommendedSongs() {
            PlaylistSong recommendedSong = Mockito.mock(PlaylistSong.class);
            given(recommendedSong.isRecommended()).willReturn(true);

            PlaylistSong normalSong = Mockito.mock(PlaylistSong.class);
            given(normalSong.isRecommended()).willReturn(false);

            Playlist playlist = Mockito.mock(Playlist.class);
            given(playlist.getSongs()).willReturn(new ArrayList<>(List.of(recommendedSong, normalSong)));

            Store store = Mockito.mock(Store.class);
            given(store.getPlaylist()).willReturn(playlist);

            willDoNothing().given(playlistSongRepository).deleteAll(any());

            playlistService.deleteRecommendedSongs(store);

            then(playlistSongRepository).should().deleteAll(List.of(recommendedSong));
        }
    }

    @Nested
    @DisplayName("addRecommendedSong 메서드 실행 시")
    class AddRecommendedSong {

        @Test
        @DisplayName("실패 : 구독이 활성화되지 않은 매장이면 예외를 던진다.")
        void addRecommendedSongWithInactiveStore() {
            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(true);
            SongRecommendation recommendation = Mockito.mock(SongRecommendation.class);

            Exception exception = catchException(
                () -> playlistService.addRecommendedSong(store, recommendation));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 플레이리스트가 없는 매장이면 예외를 던진다.")
        void addRecommendedSongWithNullPlaylist() {
            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(null);
            SongRecommendation recommendation = Mockito.mock(SongRecommendation.class);

            Exception exception = catchException(
                () -> playlistService.addRecommendedSong(store, recommendation));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("성공 : 추천 곡을 플레이리스트에 추가한다.")
        void addRecommendedSongSuccess() {
            SongRecommendation recommendation = Mockito.mock(SongRecommendation.class);
            given(recommendation.getVid()).willReturn("5zAEiu3SaO4");
            given(recommendation.getPlayTime()).willReturn(259);
            given(recommendation.getTitle()).willReturn("안녕 나의 사랑");
            given(recommendation.getArtist()).willReturn("성시경");

            StoreBusinessHour businessHour = Mockito.mock(StoreBusinessHour.class);
            given(businessHour.getDayOfWeek()).willReturn(LocalDate.now().getDayOfWeek().getValue());
            given(businessHour.isClosed()).willReturn(false);
            given(businessHour.getOpenTime()).willReturn(LocalTime.of(7, 0));

            PlaylistSong currentSong = Mockito.mock(PlaylistSong.class);
            given(currentSong.getPlaylistSongId()).willReturn(42L);
            given(currentSong.getPlayTime()).willReturn(86400);
            given(currentSong.getTitle()).willReturn("현재 곡");
            given(currentSong.getArtist()).willReturn("아티스트");
            given(currentSong.getVid()).willReturn("current-vid");

            Playlist playlist = Mockito.mock(Playlist.class);
            given(playlist.getPlaylistId()).willReturn(1L);
            given(playlist.getSongs()).willReturn(List.of(currentSong));

            PlaylistSong repoSong = Mockito.mock(PlaylistSong.class);
            given(repoSong.getPlaylistSongId()).willReturn(42L);
            given(repoSong.getPlayOrder()).willReturn(10.0);

            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(playlist);
            given(store.getBusinessHours()).willReturn(List.of(businessHour));

            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(repoSong));

            PlaylistSong result = playlistService.addRecommendedSong(store, recommendation);

            assertThat(result).isNotNull();
            assertThat(result.isRecommended()).isTrue();
        }

        @Test
        @DisplayName("성공 : 노래가 6곡 이상일 때 삽입 위치 계산이 성공한다.")
        void addRecommendedSongWithManySongs() {
            SongRecommendation recommendation = Mockito.mock(SongRecommendation.class);
            given(recommendation.getVid()).willReturn("5zAEiu3SaO4");
            given(recommendation.getPlayTime()).willReturn(259);
            given(recommendation.getTitle()).willReturn("안녕 나의 사랑");
            given(recommendation.getArtist()).willReturn("성시경");

            StoreBusinessHour businessHour = Mockito.mock(StoreBusinessHour.class);
            given(businessHour.getDayOfWeek()).willReturn(LocalDate.now().getDayOfWeek().getValue());
            given(businessHour.isClosed()).willReturn(false);
            given(businessHour.getOpenTime()).willReturn(LocalTime.of(7, 0));

            // The playlist song that CurrentSongResolver will find (ID=1, matches repoSong1)
            PlaylistSong resolverSong = Mockito.mock(PlaylistSong.class);
            given(resolverSong.getPlaylistSongId()).willReturn(1L);
            given(resolverSong.getPlayTime()).willReturn(86400);
            given(resolverSong.getTitle()).willReturn("현재 곡");
            given(resolverSong.getArtist()).willReturn("아티스트");
            given(resolverSong.getVid()).willReturn("current-vid");

            Playlist playlist = Mockito.mock(Playlist.class);
            given(playlist.getPlaylistId()).willReturn(1L);
            given(playlist.getSongs()).willReturn(List.of(resolverSong));

            // 6 repo songs: current at index 0, so nextSongIndex = 5 != 0
            PlaylistSong song1 = Mockito.mock(PlaylistSong.class);
            given(song1.getPlaylistSongId()).willReturn(1L);
            Mockito.lenient().when(song1.getPlayOrder()).thenReturn(10.0);
            PlaylistSong song2 = Mockito.mock(PlaylistSong.class);
            PlaylistSong song3 = Mockito.mock(PlaylistSong.class);
            PlaylistSong song4 = Mockito.mock(PlaylistSong.class);
            PlaylistSong song5 = Mockito.mock(PlaylistSong.class);
            given(song5.getPlayOrder()).willReturn(50.0);
            PlaylistSong song6 = Mockito.mock(PlaylistSong.class);
            given(song6.getPlayOrder()).willReturn(60.0);

            Store store = Mockito.mock(Store.class);
            given(store.isInactive()).willReturn(false);
            given(store.getPlaylist()).willReturn(playlist);
            given(store.getBusinessHours()).willReturn(List.of(businessHour));

            given(playlistSongRepository.findAllByPlaylistPlaylistIdOrderByPlayOrder(1L))
                .willReturn(List.of(song1, song2, song3, song4, song5, song6));

            PlaylistSong result = playlistService.addRecommendedSong(store, recommendation);

            assertThat(result).isNotNull();
        }
    }
}

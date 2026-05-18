package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static com.verby.indp.fixture.PlaylistFixture.playlistWithSongs;
import static com.verby.indp.fixture.PlaylistSongFixture.playlistSongWithId;
import static com.verby.indp.fixture.StoreFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class CurrentSongResolverTest {

    private CurrentSongResolver createResolver(String instantUtc) {
        Clock clock = Clock.fixed(Instant.parse(instantUtc), ZoneId.of("Asia/Seoul"));
        return new CurrentSongResolver(clock);
    }

    @Nested
    @DisplayName("resolveCurrentSong 메서드 실행 시")
    class ResolveCurrentSong {

        @Test
        @DisplayName("플레이리스트가 없으면 빈 값을 반환한다.")
        void returnEmptyWhenNoPlaylist() {
            // given
            CurrentSongResolver resolver = createResolver("2026-04-24T03:00:00Z");
            Store store = store();

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("영업시간 외에는 빈 값을 반환한다.")
        void returnEmptyWhenClosed() {
            // given
            CurrentSongResolver resolver = createResolver("2026-04-24T03:00:00Z");
            Store store = storeWithPlaylist(playlistWithSongs(
                List.of(playlistSongWithId(1L, 10.0, 300))));
            // closedStore는 모든 요일 휴무
            Store closed = closedStore();
            // playlist 설정
            org.springframework.test.util.ReflectionTestUtils.setField(
                closed, "playlist", store.getPlaylist());

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(closed);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("영업시간 내에 현재 곡을 반환한다.")
        void returnCurrentSongDuringBusinessHours() {
            // given — KST 금요일 12:00, 매장 10:00~22:00 영업
            CurrentSongResolver resolver = createResolver("2026-04-24T03:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(5, LocalTime.of(10, 0), LocalTime.of(22, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().playlistSongId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("오픈 시간 전이면 빈 값을 반환한다.")
        void returnEmptyBeforeOpenTime() {
            // given — KST 금요일 08:00, 매장 10:00~22:00 영업 (startTime=09:30)
            CurrentSongResolver resolver = createResolver("2026-04-23T23:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(5, LocalTime.of(10, 0), LocalTime.of(22, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("모든 곡의 재생 시간을 초과하면 빈 값을 반환한다.")
        void returnEmptyWhenAllSongsPlayed() {
            // given — KST 금요일 12:00, 매장 00:00~23:59 영업, 곡 총 60초
            CurrentSongResolver resolver = createResolver("2026-04-24T03:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 30);
            PlaylistSong song2 = playlistSongWithId(2L, 20.0, 30);
            Store store = storeWithPlaylist(playlistWithSongs(List.of(song1, song2)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("자정 넘김 영업시간")
    class CrossesMidnight {

        @Test
        @DisplayName("전날 영업시간이 자정을 넘기면 현재 곡을 반환한다.")
        void returnCurrentSongWhenYesterdayCrossesMidnight() {
            // given — KST 토요일 01:00 (UTC 금요일 16:00)
            // 금요일(5) 22:00~03:00 영업
            CurrentSongResolver resolver = createResolver("2026-04-24T16:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(5, LocalTime.of(22, 0), LocalTime.of(3, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("전날 자정 넘김 영업이 끝났으면 빈 값을 반환한다.")
        void returnEmptyWhenYesterdayOverflowEnded() {
            // given — KST 토요일 04:00 (UTC 금요일 19:00)
            // 금요일(5) 22:00~03:00 영업 → 03:00 이후이므로 영업 종료
            CurrentSongResolver resolver = createResolver("2026-04-24T19:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(5, LocalTime.of(22, 0), LocalTime.of(3, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("일요일에서 월요일로 넘어가는 자정 넘김을 처리한다.")
        void handleSundayToMondayCrossMidnight() {
            // given — KST 월요일 01:00 (dayOfWeek=1), 전날=일요일(7)
            // 일요일(7) 20:00~02:00 영업
            // 2026-04-27 = 월요일, UTC 2026-04-26T16:00:00Z = KST 월요일 01:00
            CurrentSongResolver resolver = createResolver("2026-04-26T16:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(7, LocalTime.of(20, 0), LocalTime.of(2, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isPresent();
        }

        @Test
        @DisplayName("전날 영업시간이 자정을 넘기지 않으면 빈 값을 반환한다.")
        void returnEmptyWhenYesterdayDoesNotCrossMidnight() {
            // given — KST 토요일 01:00 (UTC 금요일 16:00)
            // 금요일(5) 10:00~22:00 영업 (자정 안 넘김)
            CurrentSongResolver resolver = createResolver("2026-04-24T16:00:00Z");
            PlaylistSong song1 = playlistSongWithId(1L, 10.0, 86400);
            List<BusinessHour> hours = List.of(
                new BusinessHour(5, LocalTime.of(10, 0), LocalTime.of(22, 0), false));
            Store store = createStoreWithHoursAndPlaylist(hours,
                playlistWithSongs(List.of(song1)));

            // when
            Optional<CurrentSong> result = resolver.resolveCurrentSong(store);

            // then
            assertThat(result).isEmpty();
        }
    }

    private Store createStoreWithHoursAndPlaylist(
        List<BusinessHour> hours,
        com.verby.indp.domain.playlist.Playlist playlist) {
        Store store = createStoreWithHours(hours);
        org.springframework.test.util.ReflectionTestUtils.setField(store, "playlist", playlist);
        return store;
    }
}

package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.fixture.StoreFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FixedPlaylistSongTest {

    private Store store() {
        return StoreFixture.store();
    }

    private final LocalDate start = LocalDate.of(2026, 6, 1);
    private final LocalDate end = LocalDate.of(2026, 6, 30);

    @Nested
    @DisplayName("FixedPlaylistSong 생성 시")
    class NewFixedPlaylistSong {

        @Test
        @DisplayName("성공 : 랜덤 모드(시간대 지정) 특정곡을 생성한다.")
        void newFixedPlaylistSongWithHour() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 엑셀 모드(시간대 없음) 특정곡을 생성한다.")
        void newFixedPlaylistSongWithoutHour() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, null, 25,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : store가 null이면 예외를 던진다.")
        void nullStore() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(null, start, end, 14, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : startDate가 null이면 예외를 던진다.")
        void nullStartDate() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), null, end, 14, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : endDate가 startDate보다 빠르면 예외를 던진다.")
        void endBeforeStart() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), end, start, 14, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : targetHour가 0~23 범위를 벗어나면 예외를 던진다.")
        void invalidTargetHour() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 24, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : position이 1 미만이면 예외를 던진다.")
        void positionBelowOne() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 0,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 랜덤 모드에서 position이 18을 초과하면 예외를 던진다.")
        void positionOver18WithHour() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 19,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 비어있으면 예외를 던진다.")
        void blankTitle() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 3,
                    " ", "성시경", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : artist가 비어있으면 예외를 던진다.")
        void blankArtist() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 3,
                    "안녕 나의 사랑", " ", "5zAEiu3SaO4", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : vid가 비어있으면 예외를 던진다.")
        void blankVid() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 3,
                    "안녕 나의 사랑", "성시경", " ", 259));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 0 이하이면 예외를 던진다.")
        void nonPositivePlayTime() {
            // when
            Exception exception = catchException(() ->
                new FixedPlaylistSong(store(), start, end, 14, 3,
                    "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

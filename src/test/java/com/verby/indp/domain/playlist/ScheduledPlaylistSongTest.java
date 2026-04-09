package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ScheduledPlaylistSongTest {

    @Nested
    @DisplayName("ScheduledPlaylistSong 생성 시")
    class NewScheduledPlaylistSong {

        @Test
        @DisplayName("성공 : ScheduledPlaylistSong을 생성한다.")
        void newScheduledPlaylistSong() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, 10.0));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : vid가 blank이면 예외를 던진다.")
        void newScheduledPlaylistSongWithBlankVid() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "  ", 259, 10.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 null이면 예외를 던진다.")
        void newScheduledPlaylistSongWithNullPlayTime() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", null, 10.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 0 이하이면 예외를 던진다.")
        void newScheduledPlaylistSongWithNonPositivePlayTime() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 0, 10.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 blank이면 예외를 던진다.")
        void newScheduledPlaylistSongWithBlankTitle() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("  ", "성시경", "5zAEiu3SaO4", 259, 10.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : artist가 blank이면 예외를 던진다.")
        void newScheduledPlaylistSongWithBlankArtist() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "  ", "5zAEiu3SaO4", 259, 10.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playOrder가 null이면 예외를 던진다.")
        void newScheduledPlaylistSongWithNullPlayOrder() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, null));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playOrder가 0 이하이면 예외를 던진다.")
        void newScheduledPlaylistSongWithNonPositivePlayOrder() {
            Exception exception = catchException(() ->
                new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, 0.0));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

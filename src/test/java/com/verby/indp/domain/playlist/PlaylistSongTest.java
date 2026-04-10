package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaylistSongTest {

    @Nested
    @DisplayName("PlaylistSong 생성 시")
    class NewPlaylistSong {

        @Test
        @DisplayName("성공 : PlaylistSong을 생성한다.")
        void newPlaylistSong() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "성시경", 10.0));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : vid가 blank이면 예외를 던진다.")
        void newPlaylistSongWithBlankVid() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "  ", 259, "안녕 나의 사랑", "성시경", 10.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 null이면 예외를 던진다.")
        void newPlaylistSongWithNullPlayTime() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", null, "안녕 나의 사랑", "성시경", 10.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playTime이 0 이하이면 예외를 던진다.")
        void newPlaylistSongWithNonPositivePlayTime() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", 0, "안녕 나의 사랑", "성시경", 10.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 blank이면 예외를 던진다.")
        void newPlaylistSongWithBlankTitle() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "  ", "성시경", 10.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : artist가 blank이면 예외를 던진다.")
        void newPlaylistSongWithBlankArtist() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "  ", 10.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : playOrder가 0 이하이면 예외를 던진다.")
        void newPlaylistSongWithNonPositivePlayOrder() {
            // when
            Exception exception = catchException(() ->
                new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "성시경", 0.0));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

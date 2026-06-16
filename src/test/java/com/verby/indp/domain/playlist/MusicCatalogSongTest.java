package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MusicCatalogSongTest {

    @Nested
    @DisplayName("MusicCatalogSong 생성 시")
    class NewMusicCatalogSong {

        @Test
        @DisplayName("성공 : 음원 카탈로그 곡을 생성한다.")
        void newMusicCatalogSong() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong("잔잔한", 1, "안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : artist/playTime/vid는 비어있어도 생성된다.")
        void newMusicCatalogSongWithBlankOptionalFields() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong("잔잔한", 1, "안녕 나의 사랑", "", "", ""));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : mood가 null이면 예외를 던진다.")
        void nullMood() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong(null, 1, "안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : mood가 공백이면 예외를 던진다.")
        void blankMood() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong(" ", 1, "안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : position이 1 미만이면 예외를 던진다.")
        void positionBelowOne() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong("잔잔한", 0, "안녕 나의 사랑", "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 null이면 예외를 던진다.")
        void nullTitle() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong("잔잔한", 1, null, "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : title이 공백이면 예외를 던진다.")
        void blankTitle() {
            // when
            Exception exception = catchException(() ->
                new MusicCatalogSong("잔잔한", 1, " ", "성시경", "4:19", "5zAEiu3SaO4"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

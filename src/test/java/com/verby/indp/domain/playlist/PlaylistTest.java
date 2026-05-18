package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaylistTest {

    private PlaylistSong song() {
        return new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "성시경", 10.0);
    }

    @Nested
    @DisplayName("Playlist 생성 시")
    class NewPlaylist {

        @Test
        @DisplayName("성공 : Playlist를 생성한다.")
        void newPlaylist() {
            // when
            Exception exception = catchException(() -> new Playlist(List.of(song())));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : songs가 null이면 예외를 던진다.")
        void newPlaylistWithNullSongs() {
            // when
            Exception exception = catchException(() -> new Playlist(null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : songs가 비어있으면 예외를 던진다.")
        void newPlaylistWithEmptySongs() {
            // when
            Exception exception = catchException(() -> new Playlist(List.of()));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("addSong 메서드 실행 시")
    class AddSong {

        @Test
        @DisplayName("성공 : 곡을 추가한다.")
        void addSong() {
            // given
            Playlist playlist = new Playlist(new ArrayList<>(List.of(song())));
            PlaylistSong newSong = song();

            // when
            playlist.addSong(newSong);

            // then
            assertThat(playlist.getSongs()).hasSize(2);
        }
    }
}

package com.verby.indp.domain.playlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.fixture.StoreFixture;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ScheduledPlaylistTest {

    private Store store() {
        return StoreFixture.store();
    }

    private ScheduledPlaylistSong song() {
        return new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, 10.0);
    }

    @Nested
    @DisplayName("ScheduledPlaylist 생성 시")
    class NewScheduledPlaylist {

        @Test
        @DisplayName("성공 : ScheduledPlaylist를 생성한다.")
        void newScheduledPlaylist() {
            Exception exception = catchException(() ->
                new ScheduledPlaylist(store(), LocalDateTime.now().plusHours(1),
                    List.of(song())));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : store가 null이면 예외를 던진다.")
        void newScheduledPlaylistWithNullStore() {
            Exception exception = catchException(() ->
                new ScheduledPlaylist(null, LocalDateTime.now().plusHours(1), List.of(song())));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : scheduledAt이 null이면 예외를 던진다.")
        void newScheduledPlaylistWithNullScheduledAt() {
            Exception exception = catchException(() ->
                new ScheduledPlaylist(store(), null, List.of(song())));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : songs가 비어있으면 예외를 던진다.")
        void newScheduledPlaylistWithEmptySongs() {
            Exception exception = catchException(() ->
                new ScheduledPlaylist(store(), LocalDateTime.now().plusHours(1), List.of()));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("markApplied 메서드 실행 시")
    class MarkApplied {

        @Test
        @DisplayName("성공 : 상태를 APPLIED로 변경한다.")
        void markApplied() {
            ScheduledPlaylist scheduledPlaylist = new ScheduledPlaylist(store(),
                LocalDateTime.now().plusHours(1), List.of(song()));

            scheduledPlaylist.markApplied();

            assertThat(scheduledPlaylist.getStatus())
                .isEqualTo(ScheduledPlaylist.UpdateStatus.APPLIED);
        }
    }
}

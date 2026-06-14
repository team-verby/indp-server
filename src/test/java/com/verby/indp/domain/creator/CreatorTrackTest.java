package com.verby.indp.domain.creator;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreatorTrackTest {

    @Nested
    @DisplayName("CreatorTrack 생성 시")
    class NewCreatorTrack {

        @Test
        @DisplayName("성공 : 트랙을 생성한다.")
        void newTrack() {
            Creator creator = creatorWithId(1L);
            Exception exception = catchException(
                () -> new CreatorTrack(creator, "track.mp3", "https://cdn.example.com/track.mp3", "3:42", 222));
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : duration이 null이면 0:00으로 저장된다.")
        void newTrackNullDuration() {
            Creator creator = creatorWithId(1L);
            CreatorTrack track = new CreatorTrack(creator, "track.mp3", "https://cdn.example.com/track.mp3", null, 0);
            assertThat(track.getDuration()).isEqualTo("0:00");
            assertThat(track.getSecs()).isZero();
        }

        @Test
        @DisplayName("실패 : creator가 null이면 예외를 던진다.")
        void newTrackNullCreator() {
            Exception exception = catchException(
                () -> new CreatorTrack(null, "track.mp3", "https://cdn.example.com/track.mp3", "3:42", 222));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : filename이 blank이면 예외를 던진다.")
        void newTrackBlankFilename() {
            Creator creator = creatorWithId(1L);
            Exception exception = catchException(
                () -> new CreatorTrack(creator, "", "https://cdn.example.com/track.mp3", "3:42", 222));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : streamUrl이 blank이면 예외를 던진다.")
        void newTrackBlankStreamUrl() {
            Creator creator = creatorWithId(1L);
            Exception exception = catchException(
                () -> new CreatorTrack(creator, "track.mp3", "", "3:42", 222));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

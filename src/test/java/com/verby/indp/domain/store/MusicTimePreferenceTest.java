package com.verby.indp.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.fixture.StoreMusicFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MusicTimePreferenceTest {

    @Nested
    @DisplayName("MusicTimePreference 생성 시")
    class NewMusicTimePreference {

        @Test
        @DisplayName("성공 : MusicTimePreference를 생성한다.")
        void newMusicTimePreference() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, 9, 18, "CALM"));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : storeMusic이 null이면 예외를 던진다.")
        void newMusicTimePreferenceWithNullStoreMusic() {
            Exception exception = catchException(
                () -> new MusicTimePreference(null, 9, 18, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : startTimeHour가 0 미만이면 예외를 던진다.")
        void newMusicTimePreferenceWithNegativeStartTimeHour() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, -1, 18, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : startTimeHour가 23 초과이면 예외를 던진다.")
        void newMusicTimePreferenceWithStartTimeHourOver23() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, 24, 18, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : endTimeHour가 0 미만이면 예외를 던진다.")
        void newMusicTimePreferenceWithNegativeEndTimeHour() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, 9, -1, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : endTimeHour가 23 초과이면 예외를 던진다.")
        void newMusicTimePreferenceWithEndTimeHourOver23() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, 9, 24, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : startTimeHour가 endTimeHour 이상이면 예외를 던진다.")
        void newMusicTimePreferenceWithStartTimeHourGteEndTimeHour() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new MusicTimePreference(storeMusic, 18, 9, "CALM"));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

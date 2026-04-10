package com.verby.indp.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.vo.Genre;
import com.verby.indp.fixture.StoreMusicFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MusicGenreTest {

    @Nested
    @DisplayName("MusicGenre 생성 시")
    class NewMusicGenre {

        @Test
        @DisplayName("성공 : MusicGenre를 생성한다.")
        void newMusicGenre() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            // when
            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : storeMusic이 null이면 예외를 던진다.")
        void newMusicGenreWithNullStoreMusic() {
            // given & when
            Exception exception = catchException(
                () -> new MusicGenre(null, Genre.INDIE, MusicGenre.PreferenceType.LIKE));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : genre가 null이면 예외를 던진다.")
        void newMusicGenreWithNullGenre() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            // when
            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, null, MusicGenre.PreferenceType.LIKE));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : preferenceType이 null이면 예외를 던진다.")
        void newMusicGenreWithNullPreferenceType() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            // when
            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, Genre.INDIE, null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("isPreferred 메서드 실행 시")
    class IsPreferred {

        @Test
        @DisplayName("성공 : LIKE이면 true를 반환한다.")
        void isPreferredTrue() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE);

            // when & then
            assertThat(genre.isPreferred()).isTrue();
        }

        @Test
        @DisplayName("성공 : DISLIKE이면 false를 반환한다.")
        void isPreferredFalse() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE,
                MusicGenre.PreferenceType.DISLIKE);

            // when & then
            assertThat(genre.isPreferred()).isFalse();
        }
    }

    @Nested
    @DisplayName("isRejected 메서드 실행 시")
    class IsRejected {

        @Test
        @DisplayName("성공 : DISLIKE이면 true를 반환한다.")
        void isRejectedTrue() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE,
                MusicGenre.PreferenceType.DISLIKE);

            // when & then
            assertThat(genre.isRejected()).isTrue();
        }

        @Test
        @DisplayName("성공 : LIKE이면 false를 반환한다.")
        void isRejectedFalse() {
            // given
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE);

            // when & then
            assertThat(genre.isRejected()).isFalse();
        }
    }
}

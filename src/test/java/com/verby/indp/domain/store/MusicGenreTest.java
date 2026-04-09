package com.verby.indp.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.vo.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MusicGenreTest {

    @Nested
    @DisplayName("MusicGenre 생성 시")
    class NewMusicGenre {

        @Test
        @DisplayName("성공 : MusicGenre를 생성한다.")
        void newMusicGenre() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);

            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : storeMusic이 null이면 예외를 던진다.")
        void newMusicGenreWithNullStoreMusic() {
            Exception exception = catchException(
                () -> new MusicGenre(null, Genre.INDIE, MusicGenre.PreferenceType.LIKE));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : genre가 null이면 예외를 던진다.")
        void newMusicGenreWithNullGenre() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);

            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, null, MusicGenre.PreferenceType.LIKE));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : preferenceType이 null이면 예외를 던진다.")
        void newMusicGenreWithNullPreferenceType() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);

            Exception exception = catchException(
                () -> new MusicGenre(storeMusic, Genre.INDIE, null));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("isPreferred 메서드 실행 시")
    class IsPreferred {

        @Test
        @DisplayName("성공 : LIKE이면 true를 반환한다.")
        void isPreferredTrue() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE);

            assertThat(genre.isPreferred()).isTrue();
        }

        @Test
        @DisplayName("성공 : DISLIKE이면 false를 반환한다.")
        void isPreferredFalse() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE,
                MusicGenre.PreferenceType.DISLIKE);

            assertThat(genre.isPreferred()).isFalse();
        }
    }

    @Nested
    @DisplayName("isRejected 메서드 실행 시")
    class IsRejected {

        @Test
        @DisplayName("성공 : DISLIKE이면 true를 반환한다.")
        void isRejectedTrue() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE,
                MusicGenre.PreferenceType.DISLIKE);

            assertThat(genre.isRejected()).isTrue();
        }

        @Test
        @DisplayName("성공 : LIKE이면 false를 반환한다.")
        void isRejectedFalse() {
            StoreMusic storeMusic = Mockito.mock(StoreMusic.class);
            MusicGenre genre = new MusicGenre(storeMusic, Genre.INDIE, MusicGenre.PreferenceType.LIKE);

            assertThat(genre.isRejected()).isFalse();
        }
    }
}

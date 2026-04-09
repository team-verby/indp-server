package com.verby.indp.domain.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.fixture.StoreMusicFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlayMethodTest {

    @Nested
    @DisplayName("PlayMethod 생성 시")
    class NewPlayMethod {

        @Test
        @DisplayName("성공 : PlayMethod를 생성한다.")
        void newPlayMethod() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(
                () -> new PlayMethod(storeMusic, PlayMethod.Method.BLUETOOTH));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : storeMusic이 null이면 예외를 던진다.")
        void newPlayMethodWithNullStoreMusic() {
            Exception exception = catchException(
                () -> new PlayMethod(null, PlayMethod.Method.BLUETOOTH));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : method가 null이면 예외를 던진다.")
        void newPlayMethodWithNullMethod() {
            StoreMusic storeMusic = StoreMusicFixture.storeMusic();

            Exception exception = catchException(() -> new PlayMethod(storeMusic, null));

            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

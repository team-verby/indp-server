package com.verby.indp.domain.creator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreatorTest {

    @Nested
    @DisplayName("Creator 생성 시")
    class NewCreator {

        @Test
        @DisplayName("성공 : Creator를 생성한다.")
        void newCreator() {
            // when
            Exception exception = catchException(
                () -> new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : name이 null이면 예외를 던진다.")
        void newCreatorWithNullName() {
            Exception exception = catchException(
                () -> new Creator(null, "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!"));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : djName이 null이면 예외를 던진다.")
        void newCreatorWithNullDjName() {
            Exception exception = catchException(
                () -> new Creator("박완", null, "010-1234-5678", "dj@example.com", "password123!"));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : phone이 null이면 예외를 던진다.")
        void newCreatorWithNullPhone() {
            Exception exception = catchException(
                () -> new Creator("박완", "DJ Parkwan", null, "dj@example.com", "password123!"));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : email이 null이면 예외를 던진다.")
        void newCreatorWithNullEmail() {
            Exception exception = catchException(
                () -> new Creator("박완", "DJ Parkwan", "010-1234-5678", null, "password123!"));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : password가 null이면 예외를 던진다.")
        void newCreatorWithNullPassword() {
            Exception exception = catchException(
                () -> new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", null));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("deactivate 메서드 실행 시")
    class Deactivate {

        @Test
        @DisplayName("성공 : 크리에이터를 비활성화한다.")
        void deactivate() {
            // given
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
            assertThat(creator.isActive()).isTrue();

            // when
            creator.deactivate();

            // then
            assertThat(creator.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("mismatchPassword 메서드 실행 시")
    class MismatchPassword {

        @Test
        @DisplayName("성공 : 비밀번호가 일치하면 false를 반환한다.")
        void mismatchPasswordFalse() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
            assertThat(creator.mismatchPassword("password123!")).isFalse();
        }

        @Test
        @DisplayName("성공 : 비밀번호가 불일치하면 true를 반환한다.")
        void mismatchPasswordTrue() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
            assertThat(creator.mismatchPassword("wrongpassword")).isTrue();
        }
    }
}

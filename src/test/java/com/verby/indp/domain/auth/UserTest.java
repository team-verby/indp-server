package com.verby.indp.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserTest {

    @Nested
    @DisplayName("User 생성 시")
    class NewUser {

        @Test
        @DisplayName("성공 : User를 생성한다.")
        void newUser() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", "password123!", "박완", "parkwan@example.com"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : email 없이 User를 생성한다.")
        void newUserWithoutEmail() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", "password123!", "박완", null));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : loginId가 null이면 예외를 던진다.")
        void newUserWithNullLoginId() {
            // when
            Exception exception = catchException(
                () -> new User(null, "password123!", "박완", "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : loginId가 blank이면 예외를 던진다.")
        void newUserWithBlankLoginId() {
            // when
            Exception exception = catchException(
                () -> new User("  ", "password123!", "박완", "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : password가 null이면 예외를 던진다.")
        void newUserWithNullPassword() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", null, "박완", "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : password가 blank이면 예외를 던진다.")
        void newUserWithBlankPassword() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", "  ", "박완", "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : name이 null이면 예외를 던진다.")
        void newUserWithNullName() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", "password123!", null, "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : name이 blank이면 예외를 던진다.")
        void newUserWithBlankName() {
            // when
            Exception exception = catchException(
                () -> new User("parkwan123", "password123!", "  ", "parkwan@example.com"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("mismatchPassword 메서드 실행 시")
    class MismatchPassword {

        @Test
        @DisplayName("성공 : 비밀번호가 일치하면 false를 반환한다.")
        void mismatchPasswordFalse() {
            org.springframework.security.crypto.password.PasswordEncoder encoder =
                org.mockito.Mockito.mock(org.springframework.security.crypto.password.PasswordEncoder.class);
            org.mockito.BDDMockito.given(encoder.matches("password123!", "password123!")).willReturn(true);
            User user = new User("parkwan123", "password123!", "박완", "parkwan@example.com");
            assertThat(user.mismatchPassword("password123!", encoder)).isFalse();
        }

        @Test
        @DisplayName("성공 : 비밀번호가 불일치하면 true를 반환한다.")
        void mismatchPasswordTrue() {
            org.springframework.security.crypto.password.PasswordEncoder encoder =
                org.mockito.Mockito.mock(org.springframework.security.crypto.password.PasswordEncoder.class);
            org.mockito.BDDMockito.given(encoder.matches("wrongpassword", "password123!")).willReturn(false);
            User user = new User("parkwan123", "password123!", "박완", "parkwan@example.com");
            assertThat(user.mismatchPassword("wrongpassword", encoder)).isTrue();
        }
    }
}

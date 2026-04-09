package com.verby.indp.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Nested
    @DisplayName("Owner 생성 시")
    class NewOwner {

        @Test
        @DisplayName("성공 : Owner를 생성한다.")
        void newOwner() {
            // when
            Exception exception = catchException(
                () -> new Owner("store0001", "password123!", "홍길동", "010-1234-5678"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : loginId가 null이면 예외를 던진다.")
        void newOwnerWithNullLoginId() {
            // when
            Exception exception = catchException(
                () -> new Owner(null, "password123!", "홍길동", "010-1234-5678"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : loginId가 blank이면 예외를 던진다.")
        void newOwnerWithBlankLoginId() {
            // when
            Exception exception = catchException(
                () -> new Owner("  ", "password123!", "홍길동", "010-1234-5678"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : password가 null이면 예외를 던진다.")
        void newOwnerWithNullPassword() {
            // when
            Exception exception = catchException(
                () -> new Owner("store0001", null, "홍길동", "010-1234-5678"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : password가 blank이면 예외를 던진다.")
        void newOwnerWithBlankPassword() {
            // when
            Exception exception = catchException(
                () -> new Owner("store0001", "  ", "홍길동", "010-1234-5678"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : name이 null이면 예외를 던진다.")
        void newOwnerWithNullName() {
            // when
            Exception exception = catchException(
                () -> new Owner("store0001", "password123!", null, "010-1234-5678"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : phone이 null이면 예외를 던진다.")
        void newOwnerWithNullPhone() {
            // when
            Exception exception = catchException(
                () -> new Owner("store0001", "password123!", "홍길동", null));

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
            // given
            Owner owner = new Owner("store0001", "password123!", "홍길동", "010-1234-5678");

            // when
            boolean result = owner.mismatchPassword("password123!");

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("성공 : 비밀번호가 불일치하면 true를 반환한다.")
        void mismatchPasswordTrue() {
            // given
            Owner owner = new Owner("store0001", "password123!", "홍길동", "010-1234-5678");

            // when
            boolean result = owner.mismatchPassword("wrongpassword");

            // then
            assertThat(result).isTrue();
        }
    }
}

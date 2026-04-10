package com.verby.indp.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.common.exception.BadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Nested
    @DisplayName("RefreshToken 생성 시")
    class NewRefreshToken {

        @Test
        @DisplayName("성공 : RefreshToken을 생성한다.")
        void newRefreshToken() {
            // when
            Exception exception = catchException(() ->
                new RefreshToken("token-value", SubjectType.OWNER, 1L,
                    LocalDateTime.now().plusDays(30)));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : token이 blank이면 예외를 던진다.")
        void newRefreshTokenWithBlankToken() {
            // when
            Exception exception = catchException(() ->
                new RefreshToken("  ", SubjectType.OWNER, 1L, LocalDateTime.now().plusDays(30)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : subjectType이 null이면 예외를 던진다.")
        void newRefreshTokenWithNullSubjectType() {
            // when
            Exception exception = catchException(() ->
                new RefreshToken("token-value", null, 1L, LocalDateTime.now().plusDays(30)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : subjectId가 null이면 예외를 던진다.")
        void newRefreshTokenWithNullSubjectId() {
            // when
            Exception exception = catchException(() ->
                new RefreshToken("token-value", SubjectType.OWNER, null,
                    LocalDateTime.now().plusDays(30)));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : expiresAt이 null이면 예외를 던진다.")
        void newRefreshTokenWithNullExpiresAt() {
            // when
            Exception exception = catchException(() ->
                new RefreshToken("token-value", SubjectType.OWNER, 1L, null));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("isExpired 메서드 실행 시")
    class IsExpired {

        @Test
        @DisplayName("성공 : 만료되지 않은 토큰은 false를 반환한다.")
        void isExpiredFalse() {
            // given
            RefreshToken token = new RefreshToken("token-value", SubjectType.OWNER, 1L,
                LocalDateTime.now().plusDays(30));

            // when & then
            assertThat(token.isExpired()).isFalse();
        }

        @Test
        @DisplayName("성공 : 만료된 토큰은 true를 반환한다.")
        void isExpiredTrue() {
            // given
            RefreshToken token = new RefreshToken("token-value", SubjectType.OWNER, 1L,
                LocalDateTime.now().minusSeconds(1));

            // when & then
            assertThat(token.isExpired()).isTrue();
        }
    }
}

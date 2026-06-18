package com.verby.indp.domain.creator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "password123!");
            assertThat(creator.isActive()).isTrue();
            creator.deactivate();
            assertThat(creator.isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("startLive / stopLive 메서드 실행 시")
    class LiveStatus {

        @Test
        @DisplayName("성공 : 라이브를 시작하면 isLive가 true가 된다.")
        void startLive() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            assertThat(creator.isLive()).isFalse();
            creator.startLive();
            assertThat(creator.isLive()).isTrue();
        }

        @Test
        @DisplayName("성공 : 라이브를 종료하면 isLive가 false가 된다.")
        void stopLive() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            creator.startLive();
            creator.stopLive();
            assertThat(creator.isLive()).isFalse();
        }
    }

    @Nested
    @DisplayName("updateProfile 메서드 실행 시")
    class UpdateProfile {

        @Test
        @DisplayName("성공 : djName을 업데이트한다.")
        void updateProfileDjName() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            creator.updateProfile("DJ New", null, null);
            assertThat(creator.getDjName()).isEqualTo("DJ New");
        }

        @Test
        @DisplayName("성공 : djName이 blank이면 변경하지 않는다.")
        void updateProfileBlankDjName() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            creator.updateProfile("", "https://cdn.example.com/thumb.jpg", null);
            assertThat(creator.getDjName()).isEqualTo("DJ Parkwan");
            assertThat(creator.getThumbnailUrl()).isEqualTo("https://cdn.example.com/thumb.jpg");
        }

        @Test
        @DisplayName("성공 : 소개글을 업데이트한다.")
        void updateProfileIntroduction() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            creator.updateProfile(null, null, "잔잔한 카페 음악을 들려드립니다.");
            assertThat(creator.getIntroduction()).isEqualTo("잔잔한 카페 음악을 들려드립니다.");
        }

        @Test
        @DisplayName("실패 : 소개글이 1000자를 초과하면 예외를 던진다.")
        void updateProfileTooLongIntroduction() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            String tooLong = "가".repeat(1001);
            assertThatThrownBy(() -> creator.updateProfile(null, null, tooLong))
                .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("성공 : 썸네일을 제거한다.")
        void removeThumbnail() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "pw");
            creator.updateProfile(null, "https://cdn.example.com/thumb.jpg", null);
            creator.removeThumbnail();
            assertThat(creator.getThumbnailUrl()).isNull();
        }
    }

    @Nested
    @DisplayName("changePassword 메서드 실행 시")
    class ChangePassword {

        @Test
        @DisplayName("성공 : 비밀번호를 변경한다.")
        void changePassword() {
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "oldPw");
            creator.changePassword("newHashedPw");
            assertThat(creator.getPassword()).isEqualTo("newHashedPw");
        }
    }

    @Nested
    @DisplayName("mismatchPassword 메서드 실행 시")
    class MismatchPassword {

        @Test
        @DisplayName("성공 : 비밀번호가 일치하면 false를 반환한다.")
        void mismatchPasswordFalse() {
            PasswordEncoder encoder = mock(PasswordEncoder.class);
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "hashed-pw");
            given(encoder.matches("password123!", "hashed-pw")).willReturn(true);
            assertThat(creator.mismatchPassword("password123!", encoder)).isFalse();
        }

        @Test
        @DisplayName("성공 : 비밀번호가 불일치하면 true를 반환한다.")
        void mismatchPasswordTrue() {
            PasswordEncoder encoder = mock(PasswordEncoder.class);
            Creator creator = new Creator("박완", "DJ Parkwan", "010-1234-5678", "dj@example.com", "hashed-pw");
            given(encoder.matches("wrongpassword", "hashed-pw")).willReturn(false);
            assertThat(creator.mismatchPassword("wrongpassword", encoder)).isTrue();
        }
    }
}

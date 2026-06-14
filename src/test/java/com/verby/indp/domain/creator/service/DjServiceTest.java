package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.ChangePasswordRequest;
import com.verby.indp.domain.creator.dto.response.DjProfileResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.global.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DjServiceTest {

    @InjectMocks
    private DjService djService;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageService imageService;

    @Nested
    @DisplayName("getProfile 메서드 실행 시")
    class GetProfile {

        @Test
        @DisplayName("성공 : DJ 프로필을 반환한다.")
        void getProfile() {
            Creator creator = creatorWithId(1L);
            DjProfileResponse response = djService.getProfile(creator);
            assertThat(response.djName()).isEqualTo("DJ Parkwan");
            assertThat(response.email()).isEqualTo("dj@example.com");
        }
    }

    @Nested
    @DisplayName("updateProfile 메서드 실행 시")
    class UpdateProfile {

        @Test
        @DisplayName("성공 : 썸네일 없이 djName만 변경한다.")
        void updateProfileWithoutThumbnail() {
            Creator creator = creatorWithId(1L);
            Exception exception = catchException(
                () -> djService.updateProfile(creator,
                    new com.verby.indp.domain.creator.dto.request.UpdateDjProfileRequest("DJ New", null)));
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 썸네일과 함께 프로필을 변경한다.")
        void updateProfileWithThumbnail() {
            Creator creator = creatorWithId(1L);
            org.springframework.mock.web.MockMultipartFile thumbnail =
                new org.springframework.mock.web.MockMultipartFile(
                    "thumbnail", "thumb.jpg", "image/jpeg", new byte[]{1, 2, 3});
            given(imageService.uploadImage(thumbnail)).willReturn("https://cdn.example.com/thumb.jpg");

            Exception exception = catchException(
                () -> djService.updateProfile(creator,
                    new com.verby.indp.domain.creator.dto.request.UpdateDjProfileRequest("DJ New", thumbnail)));
            assertThat(exception).isNull();
        }
    }

    @Nested
    @DisplayName("changePassword 메서드 실행 시")
    class ChangePassword {

        @Test
        @DisplayName("성공 : 비밀번호를 변경한다.")
        void changePassword() {
            Creator creator = creatorWithId(1L);
            given(passwordEncoder.matches("password123!", "password123!")).willReturn(true);
            given(passwordEncoder.encode("newPassword1!")).willReturn("$2a$hashed-new");

            Exception exception = catchException(
                () -> djService.changePassword(creator, new ChangePasswordRequest("password123!", "newPassword1!")));

            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 현재 비밀번호가 불일치하면 예외를 던진다.")
        void changePasswordWithWrongCurrent() {
            Creator creator = creatorWithId(1L);
            given(passwordEncoder.matches("wrongPassword", "password123!")).willReturn(false);

            Exception exception = catchException(
                () -> djService.changePassword(creator, new ChangePasswordRequest("wrongPassword", "newPassword1!")));

            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }
}

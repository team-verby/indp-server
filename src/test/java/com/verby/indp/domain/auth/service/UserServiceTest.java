package com.verby.indp.domain.auth.service;

import static com.verby.indp.fixture.UserFixture.user;
import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.UnifiedLoginResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.common.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("login 메서드 실행 시")
    class Login {

        @Test
        @DisplayName("성공 : Plan A 구독자가 로그인한다.")
        void login() {
            // given
            User user = userWithId(1L);
            LoginRequest request = new LoginRequest("parkwan123", "password123!");
            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.USER, 1L,
                LocalDateTime.now().plusDays(30));

            given(userRepository.findByLoginId("parkwan123")).willReturn(Optional.of(user));
            given(passwordEncoder.matches("password123!", "password123!")).willReturn(true);
            given(authTokenService.createUserToken(1L)).willReturn("access-token");
            given(authTokenService.issueUserRefreshToken(1L)).willReturn(refreshToken);

            // when
            UnifiedLoginResponse response = userService.login(request);

            // then
            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.refreshToken()).isEqualTo("refresh-token");
            assertThat(response.planType()).isEqualTo("PLAN_A");
            assertThat(response.storeId()).isNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 아이디이면 예외를 던진다.")
        void loginWithNotExistLoginId() {
            // given
            given(userRepository.findByLoginId("unknown")).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> userService.login(new LoginRequest("unknown", "password123!")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 비밀번호가 불일치하면 예외를 던진다.")
        void loginWithWrongPassword() {
            // given
            User user = userWithId(1L);
            given(userRepository.findByLoginId("parkwan123")).willReturn(Optional.of(user));

            // when
            Exception exception = catchException(
                () -> userService.login(new LoginRequest("parkwan123", "wrongpassword")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }

    @Nested
    @DisplayName("checkEmailDuplicate 메서드 실행 시")
    class CheckEmailDuplicate {

        @Test
        @DisplayName("성공 : 사용 가능한 이메일이면 예외를 던지지 않는다.")
        void checkEmailAvailable() {
            given(userRepository.existsByEmail("new@example.com")).willReturn(false);
            Exception exception = catchException(() -> userService.checkEmailDuplicate("new@example.com"));
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 이메일이면 예외를 던진다.")
        void checkEmailDuplicate() {
            given(userRepository.existsByEmail("dup@example.com")).willReturn(true);
            Exception exception = catchException(() -> userService.checkEmailDuplicate("dup@example.com"));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 이메일이 blank이면 예외를 던진다.")
        void checkEmailBlank() {
            Exception exception = catchException(() -> userService.checkEmailDuplicate(""));
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("checkLoginIdDuplicate 메서드 실행 시")
    class CheckLoginIdDuplicate {

        @Test
        @DisplayName("성공 : 사용 가능한 아이디이면 예외를 던지지 않는다.")
        void checkLoginIdDuplicate() {
            // given
            given(userRepository.existsByLoginId("newuser")).willReturn(false);

            // when
            Exception exception = catchException(
                () -> userService.checkLoginIdDuplicate("newuser"));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : 이미 사용 중인 아이디이면 예외를 던진다.")
        void checkLoginIdDuplicateWithExist() {
            // given
            given(userRepository.existsByLoginId("parkwan123")).willReturn(true);

            // when
            Exception exception = catchException(
                () -> userService.checkLoginIdDuplicate("parkwan123"));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : loginId가 blank이면 예외를 던진다.")
        void checkLoginIdDuplicateWithBlank() {
            // when
            Exception exception = catchException(
                () -> userService.checkLoginIdDuplicate("  "));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

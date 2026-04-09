package com.verby.indp.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.common.exception.AuthException;
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
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AuthTokenService authTokenService;

    @Nested
    @DisplayName("login 메서드 실행 시")
    class Login {

        @Test
        @DisplayName("성공 : 어드민이 로그인하면 토큰을 반환한다.")
        void login() {
            // given
            Admin admin = org.mockito.Mockito.mock(Admin.class);
            given(admin.getAdminId()).willReturn(1L);
            given(admin.mismatchPassword("password123!")).willReturn(false);

            given(adminRepository.findByLoginId("admin")).willReturn(Optional.of(admin));
            given(authTokenService.createAdminToken(1L)).willReturn("access-token");

            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.ADMIN, 1L,
                LocalDateTime.now().plusDays(30));
            given(authTokenService.issueAdminRefreshToken(1L)).willReturn(refreshToken);

            LoginRequest request = new LoginRequest("admin", "password123!");

            // when
            LoginResponse result = adminService.login(request);

            // then
            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 로그인 아이디면 예외를 던진다.")
        void loginWithNotExistLoginId() {
            // given
            given(adminRepository.findByLoginId("wrong")).willReturn(Optional.empty());
            LoginRequest request = new LoginRequest("wrong", "password123!");

            // when
            Exception exception = catchException(() -> adminService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 비밀번호가 틀리면 예외를 던진다.")
        void loginWithWrongPassword() {
            // given
            Admin admin = org.mockito.Mockito.mock(Admin.class);
            given(admin.mismatchPassword("wrong")).willReturn(true);
            given(adminRepository.findByLoginId("admin")).willReturn(Optional.of(admin));

            LoginRequest request = new LoginRequest("admin", "wrong");

            // when
            Exception exception = catchException(() -> adminService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }
}

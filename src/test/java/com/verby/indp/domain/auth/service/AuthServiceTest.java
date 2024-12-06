package com.verby.indp.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.fixture.AdminFixture;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.global.jwt.TokenManager;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private TokenManager tokenManager;

    @Nested
    @DisplayName("login 메서드 실행 시")
    class Login {

        @Test
        @DisplayName("성공 : 로그인에 성공한다.")
        void login() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            LoginRequest request = new LoginRequest(admin.getUserId(), admin.getPassword());
            String token = "token";
            LoginResponse expected = new LoginResponse(token);

            when(adminRepository.findByUserId(any())).thenReturn(Optional.of(admin));
            when(tokenManager.createToken(any())).thenReturn(token);

            // when
            LoginResponse result = authService.login(request);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(AuthException) : 비밀번호가 일치하지 않을 경우 예외가 발생한다.")
        void exceptionWhenInvalid() throws Exception {
            // given
            Admin admin = AdminFixture.admin();
            LoginRequest request = new LoginRequest(admin.getUserId(), UUID.randomUUID().toString());

            when(adminRepository.findByUserId(any())).thenReturn(Optional.of(admin));

            // when
            Exception exception = catchException(() -> authService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("예외(AuthException) : 존재하지 않은 어드민일 경우 예외가 발생한다.")
        void exceptionWhenAdminNotFound() {
            // given
            LoginRequest request = new LoginRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            when(adminRepository.findByUserId(any())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> authService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

    }

}

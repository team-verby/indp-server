package com.verby.indp.domain.auth.service;

import static com.verby.indp.fixture.OwnerFixture.owner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.OwnerRepository;
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
class OwnerServiceTest {

    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private AuthTokenService authTokenService;

    @Nested
    @DisplayName("login 메서드 실행 시")
    class Login {

        @Test
        @DisplayName("성공 : 점주가 로그인하면 토큰을 반환한다.")
        void login() {
            // given
            Owner owner = owner();
            given(ownerRepository.findByLoginId("store0001")).willReturn(Optional.of(owner));
            given(authTokenService.createOwnerToken(any())).willReturn("access-token");

            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.OWNER, 1L,
                LocalDateTime.now().plusDays(30));
            given(authTokenService.issueOwnerRefreshToken(any())).willReturn(refreshToken);

            LoginRequest request = new LoginRequest("store0001", "password123!");

            // when
            LoginResponse result = ownerService.login(request);

            // then
            assertThat(result.accessToken()).isEqualTo("access-token");
            assertThat(result.refreshToken()).isEqualTo("refresh-token");
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 로그인 아이디면 예외를 던진다.")
        void loginWithNotExistLoginId() {
            // given
            given(ownerRepository.findByLoginId("wrong")).willReturn(Optional.empty());
            LoginRequest request = new LoginRequest("wrong", "password123!");

            // when
            Exception exception = catchException(() -> ownerService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 비밀번호가 틀리면 예외를 던진다.")
        void loginWithWrongPassword() {
            // given
            Owner owner = owner();
            given(ownerRepository.findByLoginId("store0001")).willReturn(Optional.of(owner));
            LoginRequest request = new LoginRequest("store0001", "wrongpassword");

            // when
            Exception exception = catchException(() -> ownerService.login(request));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }

    @Nested
    @DisplayName("logout 메서드 실행 시")
    class Logout {

        @Test
        @DisplayName("성공 : 점주가 로그아웃한다.")
        void logout() {
            willDoNothing().given(authTokenService).revokeOwnerRefreshToken(1L);

            ownerService.logout(1L);

            then(authTokenService).should().revokeOwnerRefreshToken(1L);
        }
    }

    @Nested
    @DisplayName("createOwner 메서드 실행 시")
    class CreateOwner {

        @Test
        @DisplayName("성공 : 점주를 생성한다.")
        void createOwner() {
            given(ownerRepository.existsByLoginId(any())).willReturn(false);
            Owner savedOwner = owner();
            given(ownerRepository.save(any())).willReturn(savedOwner);

            Owner result = ownerService.createOwner("홍길동", "010-1234-5678");

            assertThat(result).isNotNull();
        }
    }
}

package com.verby.indp.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import static com.verby.indp.fixture.AdminFixture.admin;
import static com.verby.indp.fixture.OwnerFixture.owner;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.dto.request.RefreshRequest;
import com.verby.indp.domain.auth.dto.response.RefreshResponse;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.RefreshTokenRepository;
import com.verby.indp.domain.common.exception.AuthException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.lenient;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @InjectMocks
    private AuthTokenService authTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private Clock clock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authTokenService, "secretKey",
            "test-secret-key-for-jwt-testing-minimum-length");
        lenient().when(clock.instant()).thenReturn(Instant.parse("2026-04-24T03:00:00Z"));
        lenient().when(clock.getZone()).thenReturn(ZoneId.systemDefault());
    }

    @Nested
    @DisplayName("createAdminToken 메서드 실행 시")
    class CreateAdminToken {

        @Test
        @DisplayName("성공 : 어드민 토큰을 생성한다.")
        void createAdminToken() {
            // when
            String token = authTokenService.createAdminToken(1L);

            // then
            assertThat(token).isNotNull().isNotBlank();
        }
    }

    @Nested
    @DisplayName("createOwnerToken 메서드 실행 시")
    class CreateOwnerToken {

        @Test
        @DisplayName("성공 : 점주 토큰을 생성한다.")
        void createOwnerToken() {
            // when
            String token = authTokenService.createOwnerToken(1L);

            // then
            assertThat(token).isNotNull().isNotBlank();
        }
    }

    @Nested
    @DisplayName("decodeAdminToken 메서드 실행 시")
    class DecodeAdminToken {

        @Test
        @DisplayName("성공 : 어드민 토큰을 디코딩한다.")
        void decodeAdminToken() {
            // given
            String token = authTokenService.createAdminToken(1L);

            // when
            Long adminId = authTokenService.decodeAdminToken(token);

            // then
            assertThat(adminId).isEqualTo(1L);
        }

        @Test
        @DisplayName("실패 : 유효하지 않은 토큰이면 예외를 던진다.")
        void decodeAdminTokenWithInvalidToken() {
            // when
            Exception exception = catchException(
                () -> authTokenService.decodeAdminToken("invalid-token"));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 점주 토큰으로 어드민 디코딩 시 예외를 던진다.")
        void decodeAdminTokenWithOwnerToken() {
            // given
            String ownerToken = authTokenService.createOwnerToken(1L);

            // when
            Exception exception = catchException(
                () -> authTokenService.decodeAdminToken(ownerToken));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }

    @Nested
    @DisplayName("decodeOwnerToken 메서드 실행 시")
    class DecodeOwnerToken {

        @Test
        @DisplayName("성공 : 점주 토큰을 디코딩한다.")
        void decodeOwnerToken() {
            // given
            String token = authTokenService.createOwnerToken(1L);

            // when
            Long ownerId = authTokenService.decodeOwnerToken(token);

            // then
            assertThat(ownerId).isEqualTo(1L);
        }

        @Test
        @DisplayName("실패 : 유효하지 않은 토큰이면 예외를 던진다.")
        void decodeOwnerTokenWithInvalidToken() {
            // when
            Exception exception = catchException(
                () -> authTokenService.decodeOwnerToken("invalid-token"));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }

    @Nested
    @DisplayName("refresh 메서드 실행 시")
    class Refresh {

        @Test
        @DisplayName("실패 : 존재하지 않는 리프레시 토큰이면 예외를 던진다.")
        void refreshWithNotFound() {
            // given
            given(refreshTokenRepository.findByToken("unknown")).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> authTokenService.refresh(new RefreshRequest("unknown")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 만료된 리프레시 토큰이면 예외를 던진다.")
        void refreshWithExpired() {
            // given
            RefreshToken expiredToken = new RefreshToken("expired-token", SubjectType.ADMIN, 1L,
                LocalDateTime.now().minusDays(1));
            given(refreshTokenRepository.findByToken("expired-token"))
                .willReturn(Optional.of(expiredToken));

            // when
            Exception exception = catchException(
                () -> authTokenService.refresh(new RefreshRequest("expired-token")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("성공 : 어드민 리프레시 토큰을 갱신한다.")
        void refreshAdminToken() {
            // given
            RefreshToken refreshToken = new RefreshToken("valid-token", SubjectType.ADMIN, 1L,
                LocalDateTime.now().plusDays(30));
            given(refreshTokenRepository.findByToken("valid-token"))
                .willReturn(Optional.of(refreshToken));

            Admin admin = admin();
            given(adminRepository.findById(1L)).willReturn(Optional.of(admin));

            RefreshToken newRefreshToken = new RefreshToken("new-token", SubjectType.ADMIN, 1L,
                LocalDateTime.now().plusDays(30));
            given(refreshTokenRepository.save(org.mockito.ArgumentMatchers.any()))
                .willReturn(newRefreshToken);

            // when
            RefreshResponse result = authTokenService.refresh(new RefreshRequest("valid-token"));

            // then
            assertThat(result).isNotNull();
            assertThat(result.refreshToken()).isEqualTo("new-token");
        }

        @Test
        @DisplayName("성공 : 점주 리프레시 토큰을 갱신한다.")
        void refreshOwnerToken() {
            // given
            RefreshToken refreshToken = new RefreshToken("valid-owner-token", SubjectType.OWNER, 1L,
                LocalDateTime.now().plusDays(30));
            given(refreshTokenRepository.findByToken("valid-owner-token"))
                .willReturn(Optional.of(refreshToken));

            Owner owner = owner();
            given(ownerRepository.findById(1L)).willReturn(Optional.of(owner));

            RefreshToken newRefreshToken = new RefreshToken("new-owner-token", SubjectType.OWNER, 1L,
                LocalDateTime.now().plusDays(30));
            given(refreshTokenRepository.save(org.mockito.ArgumentMatchers.any()))
                .willReturn(newRefreshToken);

            // when
            RefreshResponse result = authTokenService.refresh(new RefreshRequest("valid-owner-token"));

            // then
            assertThat(result).isNotNull();
            assertThat(result.refreshToken()).isEqualTo("new-owner-token");
        }
    }
}

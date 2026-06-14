package com.verby.indp.domain.auth.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static com.verby.indp.fixture.OwnerFixture.ownerWithId;
import static com.verby.indp.fixture.UserFixture.userWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.UnifiedLoginResponse;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnifiedAuthServiceTest {

    @InjectMocks
    private UnifiedAuthService unifiedAuthService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AuthTokenService authTokenService;

    @Nested
    @DisplayName("login 메서드 실행 시")
    class Login {

        @Test
        @DisplayName("성공 : Plan A 구독자로 로그인한다.")
        void loginAsUser() {
            // given
            User user = userWithId(1L);
            LoginRequest request = new LoginRequest("parkwan123", "password123!");
            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.USER, 1L,
                LocalDateTime.now().plusDays(30));

            given(userRepository.findByLoginId("parkwan123")).willReturn(Optional.of(user));
            given(authTokenService.createUserToken(1L)).willReturn("access-token");
            given(authTokenService.issueUserRefreshToken(1L)).willReturn(refreshToken);

            // when
            UnifiedLoginResponse response = unifiedAuthService.login(request);

            // then
            assertThat(response.planType()).isEqualTo("PLAN_A");
            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.storeId()).isNull();
        }

        @Test
        @DisplayName("성공 : 매장 관리자(Owner)로 로그인한다.")
        void loginAsOwner() {
            // given
            Owner owner = ownerWithId(1L);
            LoginRequest request = new LoginRequest("store0001", "password123!");
            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.OWNER, 1L,
                LocalDateTime.now().plusDays(30));

            given(userRepository.findByLoginId("store0001")).willReturn(Optional.empty());
            given(creatorRepository.findByEmail("store0001")).willReturn(Optional.empty());
            given(ownerRepository.findByLoginId("store0001")).willReturn(Optional.of(owner));
            given(authTokenService.createOwnerToken(1L)).willReturn("access-token");
            given(authTokenService.issueOwnerRefreshToken(1L)).willReturn(refreshToken);
            given(storeRepository.findAllByOwner(owner)).willReturn(List.of());

            // when
            UnifiedLoginResponse response = unifiedAuthService.login(request);

            // then
            assertThat(response.planType()).isEqualTo("PLAN_B");
            assertThat(response.accessToken()).isEqualTo("access-token");
        }

        @Test
        @DisplayName("성공 : 크리에이터(DJ)로 로그인한다.")
        void loginAsCreator() {
            // given
            Creator creator = creatorWithId(1L);
            LoginRequest request = new LoginRequest("dj@example.com", "password123!");
            RefreshToken refreshToken = new RefreshToken("refresh-token", SubjectType.CREATOR, 1L,
                LocalDateTime.now().plusDays(30));

            given(userRepository.findByLoginId("dj@example.com")).willReturn(Optional.empty());
            given(creatorRepository.findByEmail("dj@example.com")).willReturn(Optional.of(creator));
            given(authTokenService.createCreatorToken(1L)).willReturn("access-token");
            given(authTokenService.issueCreatorRefreshToken(1L)).willReturn(refreshToken);

            // when
            UnifiedLoginResponse response = unifiedAuthService.login(request);

            // then
            assertThat(response.planType()).isEqualTo("DJ");
            assertThat(response.accessToken()).isEqualTo("access-token");
            assertThat(response.djName()).isEqualTo("DJ Parkwan");
        }

        @Test
        @DisplayName("실패 : 비활성화된 크리에이터이면 예외를 던진다.")
        void loginWithDeactivatedCreator() {
            // given
            Creator creator = creatorWithId(1L);
            creator.deactivate();
            given(userRepository.findByLoginId("dj@example.com")).willReturn(Optional.empty());
            given(creatorRepository.findByEmail("dj@example.com")).willReturn(Optional.of(creator));

            // when
            Exception exception = catchException(
                () -> unifiedAuthService.login(new LoginRequest("dj@example.com", "password123!")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : User 비밀번호가 불일치하면 예외를 던진다.")
        void loginWithWrongUserPassword() {
            // given
            User user = userWithId(1L);
            given(userRepository.findByLoginId("parkwan123")).willReturn(Optional.of(user));

            // when
            Exception exception = catchException(
                () -> unifiedAuthService.login(new LoginRequest("parkwan123", "wrongpassword")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : Owner 비밀번호가 불일치하면 예외를 던진다.")
        void loginWithWrongOwnerPassword() {
            // given
            Owner owner = ownerWithId(1L);
            given(userRepository.findByLoginId("store0001")).willReturn(Optional.empty());
            given(creatorRepository.findByEmail("store0001")).willReturn(Optional.empty());
            given(ownerRepository.findByLoginId("store0001")).willReturn(Optional.of(owner));

            // when
            Exception exception = catchException(
                () -> unifiedAuthService.login(new LoginRequest("store0001", "wrongpassword")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 아이디이면 예외를 던진다.")
        void loginWithNotExistAccount() {
            // given
            given(userRepository.findByLoginId("unknown")).willReturn(Optional.empty());
            given(creatorRepository.findByEmail("unknown")).willReturn(Optional.empty());
            given(ownerRepository.findByLoginId("unknown")).willReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> unifiedAuthService.login(new LoginRequest("unknown", "password123!")));

            // then
            assertThat(exception).isInstanceOf(AuthException.class);
        }
    }
}

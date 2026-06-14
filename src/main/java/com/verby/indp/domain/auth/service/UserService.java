package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.UnifiedLoginResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;

    @Transactional
    public UnifiedLoginResponse login(LoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new AuthException("존재하지 않는 계정입니다."));
        if (user.mismatchPassword(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = authTokenService.createUserToken(user.getUserId());
        RefreshToken refreshToken = authTokenService.issueUserRefreshToken(user.getUserId());
        return new UnifiedLoginResponse(accessToken, refreshToken.getToken(), "PLAN_A", null, null);
    }

    public void checkLoginIdDuplicate(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new BadRequestException("loginId는 필수입니다.");
        }
        if (userRepository.existsByLoginId(loginId)) {
            throw new BadRequestException("이미 사용 중인 아이디입니다.");
        }
    }
}

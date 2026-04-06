package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthTokenService authTokenService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Admin admin = adminRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new AuthException("존재하지 않는 계정입니다."));
        if (admin.mismatchPassword(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = authTokenService.createAdminToken(admin.getAdminId());
        RefreshToken refreshToken = authTokenService.issueAdminRefreshToken(admin.getAdminId());
        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public void logout(Long adminId) {
        authTokenService.revokeAdminRefreshToken(adminId);
    }
}

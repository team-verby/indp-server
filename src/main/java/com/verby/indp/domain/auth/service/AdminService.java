package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.global.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;
    private final TokenManager tokenManager;

    public LoginResponse login(LoginRequest request) {
        Admin admin = adminRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new AuthException("존재하지 않는 계정입니다."));
        if (admin.mismatchPassword(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenManager.createAdminToken(admin.getAdminId());
        return new LoginResponse(token);
    }
}

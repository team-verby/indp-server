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
public class AuthService {

    private final AdminRepository adminRepository;
    private final TokenManager tokenManager;

    public LoginResponse login(LoginRequest request) {
        Admin admin = getAdmin(request);
        validatePassword(admin, request);

        String token = tokenManager.createToken(admin.getAdminId());
        return new LoginResponse(token);
    }

    private void validatePassword(Admin admin, LoginRequest request) {
        if (admin.mismatchPassword(request.password())) {
            throw new AuthException("아이디와 패스워드를 확인해주세요.");
        }
    }

    private Admin getAdmin(LoginRequest request) {
        return adminRepository.findByUserId(request.userId())
            .orElseThrow(() -> new AuthException("아이디와 패스워드를 확인해주세요."));
    }

}

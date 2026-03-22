package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.global.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final TokenManager tokenManager;

    public LoginResponse login(LoginRequest request) {
        Owner owner = ownerRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new AuthException("존재하지 않는 계정입니다."));
        if (owner.mismatchPassword(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenManager.createOwnerToken(owner.getOwnerId());
        return new LoginResponse(token);
    }
}

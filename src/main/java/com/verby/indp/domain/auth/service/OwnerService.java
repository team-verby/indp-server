package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.dto.request.LoginRequest;
import com.verby.indp.domain.auth.dto.response.LoginResponse;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerService {

    private static final String OWNER_LOGIN_ID_PREFIX = "store";

    private final OwnerRepository ownerRepository;
    private final AuthTokenService authTokenService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Owner owner = ownerRepository.findByLoginId(request.loginId())
            .orElseThrow(() -> new AuthException("존재하지 않는 계정입니다."));
        if (owner.mismatchPassword(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = authTokenService.createOwnerToken(owner.getOwnerId());
        RefreshToken refreshToken = authTokenService.issueOwnerRefreshToken(owner.getOwnerId());
        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    @Transactional
    public void logout(Long ownerId) {
        authTokenService.revokeOwnerRefreshToken(ownerId);
    }

    public Owner createOwner(String name, String phone) {
        String loginId = generateUniqueLoginId();
        String password = generatePassword();
        Owner owner = new Owner(loginId, password, name, phone);
        return ownerRepository.save(owner);
    }

    private String generateUniqueLoginId() {
        String loginId;
        Random random = new Random();
        do {
            loginId = OWNER_LOGIN_ID_PREFIX + String.format("%04d", random.nextInt(10000));
        } while (ownerRepository.existsByLoginId(loginId));

        return loginId;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}

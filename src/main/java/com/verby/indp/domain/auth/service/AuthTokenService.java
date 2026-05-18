package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.RefreshToken;
import com.verby.indp.domain.auth.RefreshToken.SubjectType;
import com.verby.indp.domain.auth.dto.request.RefreshRequest;
import com.verby.indp.domain.auth.dto.response.RefreshResponse;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.RefreshTokenRepository;
import com.verby.indp.domain.common.exception.AuthException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private static final long ACCESS_TOKEN_VALIDITY_MS = 60 * 60 * 1000L;
    private static final int REFRESH_TOKEN_VALIDITY_DAYS = 30;

    @Value("${jwt.secret}")
    private String secretKey;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AdminRepository adminRepository;
    private final OwnerRepository ownerRepository;
    private final Clock clock;

    public String createAdminToken(Long adminId) {
        return buildJwt("adminId", adminId);
    }

    public String createOwnerToken(Long ownerId) {
        return buildJwt("ownerId", ownerId);
    }

    public Long decodeAdminToken(String token) {
        Long adminId = decodeClaim(token, "adminId");
        if (adminId == null) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
        return adminId;
    }

    public Long decodeOwnerToken(String token) {
        Long ownerId = decodeClaim(token, "ownerId");
        if (ownerId == null) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
        return ownerId;
    }

    @Transactional
    public RefreshToken issueAdminRefreshToken(Long adminId) {
        refreshTokenRepository.deleteBySubjectTypeAndSubjectId(SubjectType.ADMIN, adminId);
        return refreshTokenRepository.save(buildRefreshToken(SubjectType.ADMIN, adminId));
    }

    @Transactional
    public RefreshToken issueOwnerRefreshToken(Long ownerId) {
        refreshTokenRepository.deleteBySubjectTypeAndSubjectId(SubjectType.OWNER, ownerId);
        return refreshTokenRepository.save(buildRefreshToken(SubjectType.OWNER, ownerId));
    }

    @Transactional
    public RefreshResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new AuthException("유효하지 않은 리프레시 토큰입니다."));

        if (refreshToken.isExpired(LocalDateTime.now(clock))) {
            refreshTokenRepository.delete(refreshToken);
            throw new AuthException("만료된 리프레시 토큰입니다.");
        }

        refreshTokenRepository.delete(refreshToken);

        SubjectType subjectType = refreshToken.getSubjectType();
        Long subjectId = refreshToken.getSubjectId();

        String newAccessToken = createAccessToken(subjectType, subjectId);
        RefreshToken newRefreshToken = refreshTokenRepository.save(
            buildRefreshToken(subjectType, subjectId));

        return new RefreshResponse(newAccessToken, newRefreshToken.getToken());
    }

    @Transactional
    public void revokeAdminRefreshToken(Long adminId) {
        refreshTokenRepository.deleteBySubjectTypeAndSubjectId(SubjectType.ADMIN, adminId);
    }

    @Transactional
    public void revokeOwnerRefreshToken(Long ownerId) {
        refreshTokenRepository.deleteBySubjectTypeAndSubjectId(SubjectType.OWNER, ownerId);
    }

    private String buildJwt(String claimKey, Long claimValue) {
        Date now = new Date();
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_MS))
            .claim(claimKey, claimValue)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    private Long decodeClaim(String token, String claimKey) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get(claimKey, Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }

    private String createAccessToken(SubjectType subjectType, Long subjectId) {
        if (subjectType == SubjectType.ADMIN) {
            adminRepository.findById(subjectId)
                .orElseThrow(() -> new AuthException("권한이 없습니다."));
            return createAdminToken(subjectId);
        }
        ownerRepository.findById(subjectId)
            .orElseThrow(() -> new AuthException("권한이 없습니다."));
        return createOwnerToken(subjectId);
    }

    private RefreshToken buildRefreshToken(SubjectType subjectType, Long subjectId) {
        return new RefreshToken(UUID.randomUUID().toString(), subjectType, subjectId,
            LocalDateTime.now(clock).plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
    }
}

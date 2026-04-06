package com.verby.indp.domain.auth.repository;

import com.verby.indp.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteBySubjectTypeAndSubjectId(RefreshToken.SubjectType subjectType, Long subjectId);
}

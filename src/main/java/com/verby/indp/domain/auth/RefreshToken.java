package com.verby.indp.domain.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject_type", nullable = false)
    private SubjectType subjectType;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public enum SubjectType {
        ADMIN, OWNER
    }

    public RefreshToken(String token, SubjectType subjectType, Long subjectId, LocalDateTime expiresAt) {
        this.token = token;
        this.subjectType = subjectType;
        this.subjectId = subjectId;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

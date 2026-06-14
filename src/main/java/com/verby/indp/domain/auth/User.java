package com.verby.indp.domain.auth;

import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(columnNames = "login_id"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    public User(String loginId, String password, String name, String email) {
        validateLoginId(loginId);
        validatePassword(password);
        validateName(name);
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean mismatchPassword(String password) {
        return !this.password.equals(password);
    }

    private void validateLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new BadRequestException("loginId는 필수입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("password는 필수입니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name은 필수입니다.");
        }
    }
}

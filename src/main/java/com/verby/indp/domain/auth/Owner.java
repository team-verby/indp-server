package com.verby.indp.domain.auth;

import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "owner", uniqueConstraints = @UniqueConstraint(columnNames = "login_id"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    public Owner(String loginId, String password, String name, String phone) {
        validateLoginId(loginId);
        validatePassword(password);
        validateName(name);
        validatePhone(phone);
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public boolean mismatchPassword(String password) {
        return !this.password.equals(password);
    }

    public void changePassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
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

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new BadRequestException("phone은 필수입니다.");
        }
    }
}

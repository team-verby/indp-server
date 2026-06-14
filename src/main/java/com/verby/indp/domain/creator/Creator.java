package com.verby.indp.domain.creator;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "creator")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Creator extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dj_name", nullable = false)
    private String djName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Creator(String name, String djName, String phone, String email, String password) {
        validateName(name);
        validateDjName(djName);
        validatePhone(phone);
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.djName = djName;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public void deactivate() {
        this.active = false;
    }

    public boolean mismatchPassword(String password) {
        return !this.password.equals(password);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("name은 필수입니다.");
        }
    }

    private void validateDjName(String djName) {
        if (djName == null || djName.isBlank()) {
            throw new BadRequestException("djName은 필수입니다.");
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new BadRequestException("phone은 필수입니다.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("email은 필수입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("password는 필수입니다.");
        }
    }
}

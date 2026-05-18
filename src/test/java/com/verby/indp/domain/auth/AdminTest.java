package com.verby.indp.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AdminTest {

    private Admin createAdmin(String loginId, String password) {
        Admin admin = new Admin();
        ReflectionTestUtils.setField(admin, "loginId", loginId);
        ReflectionTestUtils.setField(admin, "password", password);
        return admin;
    }

    @Nested
    @DisplayName("mismatchPassword 메서드 실행 시")
    class MismatchPassword {

        @Test
        @DisplayName("성공 : 비밀번호가 일치하면 false를 반환한다.")
        void mismatchPasswordFalse() {
            // given
            Admin admin = createAdmin("admin", "password123!");

            // when & then
            assertThat(admin.getLoginId()).isEqualTo("admin");
            assertThat(admin.getPassword()).isEqualTo("password123!");
            assertThat(admin.getAdminId()).isNull();
            assertThat(admin.mismatchPassword("password123!")).isFalse();
        }

        @Test
        @DisplayName("성공 : 비밀번호가 일치하지 않으면 true를 반환한다.")
        void mismatchPasswordTrue() {
            // given
            Admin admin = createAdmin("admin", "password123!");

            // when & then
            assertThat(admin.mismatchPassword("wrongpassword")).isTrue();
        }
    }
}

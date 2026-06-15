package com.verby.indp.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseMigrationConfig {

    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrate() {
        dropRefreshTokenSubjectTypeConstraint();
        dropPaymentTypeConstraint();
    }

    private void dropRefreshTokenSubjectTypeConstraint() {
        try {
            var constraints = jdbcTemplate.queryForList(
                "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS " +
                "WHERE TABLE_SCHEMA = DATABASE() " +
                "  AND TABLE_NAME = 'refresh_token' " +
                "  AND CONSTRAINT_TYPE = 'CHECK'"
            );
            for (var row : constraints) {
                String name = (String) row.get("CONSTRAINT_NAME");
                try {
                    jdbcTemplate.execute("ALTER TABLE refresh_token DROP CHECK `" + name + "`");
                    log.info("[Migration] refresh_token CHECK 제약 제거: {}", name);
                } catch (Exception ex) {
                    log.warn("[Migration] refresh_token DROP CHECK 실패 ({}): {}", name, ex.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("[Migration] refresh_token 제약 조회 실패: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute(
                "ALTER TABLE refresh_token MODIFY COLUMN subject_type VARCHAR(255) NOT NULL"
            );
            log.info("[Migration] refresh_token.subject_type 컬럼 재정의 완료");
        } catch (Exception e) {
            log.debug("[Migration] refresh_token 컬럼 재정의 스킵: {}", e.getMessage());
        }
    }

    private void dropPaymentTypeConstraint() {
        try {
            var constraints = jdbcTemplate.queryForList(
                "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS " +
                "WHERE TABLE_SCHEMA = DATABASE() " +
                "  AND TABLE_NAME = 'payment' " +
                "  AND CONSTRAINT_TYPE = 'CHECK'"
            );
            for (var row : constraints) {
                String name = (String) row.get("CONSTRAINT_NAME");
                try {
                    jdbcTemplate.execute("ALTER TABLE payment DROP CHECK `" + name + "`");
                    log.info("[Migration] payment CHECK 제약 제거: {}", name);
                } catch (Exception ex) {
                    log.warn("[Migration] payment DROP CHECK 실패 ({}): {}", name, ex.getMessage());
                }
            }
        } catch (Exception e) {
            log.warn("[Migration] payment 제약 조회 실패: {}", e.getMessage());
        }
        try {
            jdbcTemplate.execute("ALTER TABLE payment MODIFY COLUMN type VARCHAR(255) NOT NULL");
            log.info("[Migration] payment.type 컬럼 재정의 완료");
        } catch (Exception e) {
            log.debug("[Migration] payment.type 재정의 스킵: {}", e.getMessage());
        }
    }
}

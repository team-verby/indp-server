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
    }

    private void dropRefreshTokenSubjectTypeConstraint() {
        // MySQL 8.0.16+: information_schema에서 CHECK 제약 이름을 조회 후 명시적으로 DROP
        try {
            var constraints = jdbcTemplate.queryForList(
                "SELECT CONSTRAINT_NAME FROM information_schema.TABLE_CONSTRAINTS " +
                "WHERE TABLE_SCHEMA = DATABASE() " +
                "  AND TABLE_NAME = 'refresh_token' " +
                "  AND CONSTRAINT_TYPE = 'CHECK'"
            );
            for (var row : constraints) {
                String name = (String) row.get("CONSTRAINT_NAME");
                jdbcTemplate.execute("ALTER TABLE refresh_token DROP CHECK `" + name + "`");
                log.info("[Migration] refresh_token CHECK 제약 제거 완료: {}", name);
            }
            if (constraints.isEmpty()) {
                log.debug("[Migration] refresh_token CHECK 제약 없음 — 스킵");
            }
        } catch (Exception e) {
            log.warn("[Migration] refresh_token CHECK 제약 제거 실패: {}", e.getMessage());
        }
    }
}

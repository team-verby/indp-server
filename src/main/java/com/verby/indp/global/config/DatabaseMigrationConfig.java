package com.verby.indp.global.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Hibernate DDL(ddl-auto: update) 실행 전에 기존 CHECK 제약을 제거.
 * BeanFactoryPostProcessor로 구현해 EntityManagerFactory 초기화보다 먼저 실행됨.
 */
@Slf4j
@Configuration
public class DatabaseMigrationConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            DataSource dataSource = beanFactory.getBean(DataSource.class);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            dropRefreshTokenSubjectTypeConstraint(jdbcTemplate);
            dropPaymentTypeConstraint(jdbcTemplate);
        } catch (Exception e) {
            log.warn("[Migration] DataSource 초기화 전 실행 불가 — 스킵: {}", e.getMessage());
        }
    }

    private void dropRefreshTokenSubjectTypeConstraint(JdbcTemplate jdbcTemplate) {
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

    private void dropPaymentTypeConstraint(JdbcTemplate jdbcTemplate) {
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

package com.verby.indp.domain.listening.repository;

import com.verby.indp.domain.listening.ListeningDaily;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListeningDailyRepository extends JpaRepository<ListeningDaily, Long> {

    Optional<ListeningDaily> findByUserIdAndCreatorIdAndYmd(
        Long userId, Long creatorId, LocalDate ymd);

    /** 특정 크리에이터의 기간 내 총 청취초 (월 적립 집계용 — P2). */
    @Query("""
        SELECT COALESCE(SUM(l.seconds), 0)
        FROM ListeningDaily l
        WHERE l.creatorId = :creatorId
          AND l.ymd BETWEEN :from AND :to
        """)
    long sumSeconds(
        @Param("creatorId") Long creatorId,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    /** 특정 크리에이터의 전체 기간 누적 청취초 (어드민 정산 적립액 표시용). */
    @Query("""
        SELECT COALESCE(SUM(l.seconds), 0)
        FROM ListeningDaily l
        WHERE l.creatorId = :creatorId
        """)
    long sumAllSecondsByCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 실시간 청취자 수: 오늘(ymd) 행 중 최근(threshold 이후) heartbeat가 반영된 사용자 수.
     * (user_id, creator_id, ymd) 유니크이므로 행 수 = 서로 다른 사용자 수.
     */
    int countByCreatorIdAndYmdAndUpdatedAtGreaterThanEqual(
        Long creatorId, LocalDate ymd, LocalDateTime threshold);
}

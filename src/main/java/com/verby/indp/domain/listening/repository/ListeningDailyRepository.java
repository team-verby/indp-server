package com.verby.indp.domain.listening.repository;

import com.verby.indp.domain.listening.ListeningDaily;
import java.time.LocalDate;
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
}

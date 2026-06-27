package com.verby.indp.domain.settlement.repository;

import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {

    /** 처리 대기(REQUESTED) 신청 중복 방지. */
    boolean existsByCreatorIdAndStatus(Long creatorId, SettlementStatus status);

    /** 어드민 목록 — 전체를 최신 신청순으로. */
    List<SettlementRequest> findAllByOrderByRequestedAtDesc();

    /** 어드민 목록 — 상태별 최신 신청순으로. */
    List<SettlementRequest> findAllByStatusOrderByRequestedAtDesc(SettlementStatus status);

    /** 크리에이터별 특정 상태 신청 금액 합계 (누적 지급액 계산용). */
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM SettlementRequest s "
        + "WHERE s.creatorId = :creatorId AND s.status = :status")
    long sumAmountByCreatorIdAndStatus(Long creatorId, SettlementStatus status);

    /** 크리에이터의 가장 최근 처리(지급/반려) 완료된 신청 1건. */
    Optional<SettlementRequest> findFirstByCreatorIdAndProcessedAtIsNotNullOrderByProcessedAtDesc(
        Long creatorId);
}

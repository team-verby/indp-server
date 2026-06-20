package com.verby.indp.domain.settlement.repository;

import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {

    /** 처리 대기(REQUESTED) 신청 중복 방지. */
    boolean existsByCreatorIdAndStatus(Long creatorId, SettlementStatus status);

    /** 어드민 목록 — 전체를 최신 신청순으로. */
    List<SettlementRequest> findAllByOrderByRequestedAtDesc();

    /** 어드민 목록 — 상태별 최신 신청순으로. */
    List<SettlementRequest> findAllByStatusOrderByRequestedAtDesc(SettlementStatus status);
}

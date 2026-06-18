package com.verby.indp.domain.settlement.repository;

import com.verby.indp.domain.settlement.CreatorLedger;
import com.verby.indp.domain.settlement.LedgerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorLedgerRepository extends JpaRepository<CreatorLedger, Long> {

    /** 월 적립 멱등성 — 동일 적립월(ref)에 이미 적립했는지. */
    boolean existsByCreatorIdAndTypeAndRef(Long creatorId, LedgerType type, String ref);
}

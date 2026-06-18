package com.verby.indp.domain.settlement;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 적립·출금 원장 (감사 추적). 잔액 변동의 불변 기록.
 */
@Entity
@Getter
@Table(
    name = "creator_ledger",
    indexes = @Index(name = "idx_ledger_creator", columnList = "creator_id, created_at")
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreatorLedger extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creator_ledger_id")
    private Long creatorLedgerId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LedgerType type;

    /** +적립 / −출금 */
    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "balance_after", nullable = false)
    private long balanceAfter;

    /** 멱등·추적 키. 예: '2026-06'(적립월) 또는 payout_request_id */
    @Column(name = "ref", length = 100)
    private String ref;

    public CreatorLedger(Long creatorId, LedgerType type, long amount, long balanceAfter,
        String ref) {
        if (creatorId == null || creatorId <= 0) {
            throw new BadRequestException("creatorId는 필수입니다.");
        }
        if (type == null) {
            throw new BadRequestException("type은 필수입니다.");
        }
        this.creatorId = creatorId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.ref = ref;
    }
}

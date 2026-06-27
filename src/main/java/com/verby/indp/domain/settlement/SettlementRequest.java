package com.verby.indp.domain.settlement;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 크리에이터 정산(출금) 신청. 신청 시점 잔액을 스냅샷으로 보관하고, 어드민이 지급/반려 처리한다.
 * 지급(PAID) 시점에 잔액에서 차감(PAYOUT 원장 기록)한다.
 */
@Entity
@Getter
@Table(
    name = "settlement_request",
    indexes = @Index(name = "idx_settlement_creator_status", columnList = "creator_id, status")
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class SettlementRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_request_id")
    private Long settlementRequestId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    /** 신청 금액 (원) — 신청 시점 잔액 스냅샷. */
    @Column(name = "amount", nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SettlementStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    /** 신청 시점에 수집한 세금·신원·계좌 정보 스냅샷 (구버전 신청은 null일 수 있음). */
    @Embedded
    private SettlementTaxInfo taxInfo;

    public SettlementRequest(Long creatorId, long amount, LocalDateTime requestedAt) {
        this(creatorId, amount, requestedAt, null);
    }

    public SettlementRequest(
        Long creatorId, long amount, LocalDateTime requestedAt, SettlementTaxInfo taxInfo) {
        if (creatorId == null || creatorId <= 0) {
            throw new BadRequestException("creatorId는 필수입니다.");
        }
        if (amount <= 0) {
            throw new BadRequestException("신청 금액은 0보다 커야 합니다.");
        }
        if (requestedAt == null) {
            throw new BadRequestException("requestedAt은 필수입니다.");
        }
        this.creatorId = creatorId;
        this.amount = amount;
        this.status = SettlementStatus.REQUESTED;
        this.requestedAt = requestedAt;
        this.taxInfo = taxInfo;
    }

    public void markPaid(LocalDateTime processedAt) {
        ensureRequested();
        this.status = SettlementStatus.PAID;
        this.processedAt = processedAt;
    }

    public void markRejected(LocalDateTime processedAt) {
        ensureRequested();
        this.status = SettlementStatus.REJECTED;
        this.processedAt = processedAt;
    }

    private void ensureRequested() {
        if (status != SettlementStatus.REQUESTED) {
            throw new BadRequestException("이미 처리된 정산 신청입니다.");
        }
    }
}

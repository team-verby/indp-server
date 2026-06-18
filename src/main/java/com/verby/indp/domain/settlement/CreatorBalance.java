package com.verby.indp.domain.settlement;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 크리에이터 적립 잔액 (1행/크리에이터). 적립(+)·출금(−)이 누적된 현재 잔액.
 */
@Entity
@Getter
@Table(name = "creator_balance")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class CreatorBalance extends BaseTimeEntity {

    @Id
    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "balance", nullable = false)
    private long balance;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CreatorBalance(Long creatorId) {
        if (creatorId == null || creatorId <= 0) {
            throw new BadRequestException("creatorId는 필수입니다.");
        }
        this.creatorId = creatorId;
        this.balance = 0L;
    }

    /**
     * 잔액에 델타를 반영한다. (적립 +, 출금 −)
     *
     * @return 반영 후 잔액
     */
    public long apply(long delta) {
        long next = this.balance + delta;
        if (next < 0) {
            throw new BadRequestException("잔액보다 큰 금액을 출금할 수 없습니다.");
        }
        this.balance = next;
        return this.balance;
    }
}

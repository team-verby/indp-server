package com.verby.indp.domain.listening;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독자(user)가 특정 크리에이터(creator)의 음악을 하루(ymd) 동안 청취한 누적 초.
 * heartbeat 델타를 UPSERT 누적하며, 원본 비트는 저장하지 않는다.
 */
@Entity
@Getter
@Table(
    name = "listening_daily",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_listen", columnNames = {"user_id", "creator_id", "ymd"}),
    indexes = @Index(name = "idx_creator_ymd", columnList = "creator_id, ymd")
)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ListeningDaily extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listening_daily_id")
    private Long listeningDailyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(name = "ymd", nullable = false)
    private LocalDate ymd;

    @Column(name = "seconds", nullable = false)
    private int seconds;

    public ListeningDaily(Long userId, Long creatorId, LocalDate ymd, int seconds) {
        validateId(userId, "userId");
        validateId(creatorId, "creatorId");
        validateYmd(ymd);
        validateSeconds(seconds);
        this.userId = userId;
        this.creatorId = creatorId;
        this.ymd = ymd;
        this.seconds = seconds;
    }

    /**
     * 청취 델타를 누적하되 일일 상한을 넘지 않도록 클램프한다.
     *
     * @param delta    추가 청취 초 (양수)
     * @param dailyCap 일일 상한 초
     */
    public void addSeconds(int delta, int dailyCap) {
        if (delta <= 0) {
            return;
        }
        long total = (long) this.seconds + delta;
        this.seconds = (int) Math.min(total, dailyCap);
    }

    private void validateId(Long id, String field) {
        if (id == null || id <= 0) {
            throw new BadRequestException(field + "는 필수입니다.");
        }
    }

    private void validateYmd(LocalDate ymd) {
        if (ymd == null) {
            throw new BadRequestException("ymd는 필수입니다.");
        }
    }

    private void validateSeconds(int seconds) {
        if (seconds < 0) {
            throw new BadRequestException("seconds는 0 이상이어야 합니다.");
        }
    }
}

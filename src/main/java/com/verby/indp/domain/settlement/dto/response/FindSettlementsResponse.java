package com.verby.indp.domain.settlement.dto.response;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 어드민 정산 신청 목록 응답.
 *
 * @param settlements 신청 목록 (최신 신청순)
 */
public record FindSettlementsResponse(
    List<SettlementItem> settlements
) {

    /**
     * @param settlementRequestId 정산 신청 ID
     * @param creatorId           크리에이터 ID
     * @param djName              DJ(채널)명
     * @param amount              신청 금액 (원)
     * @param status              상태 (REQUESTED/PAID/REJECTED)
     * @param requestedAt         신청 일시
     * @param processedAt         처리 일시 (미처리 시 null)
     */
    public record SettlementItem(
        Long settlementRequestId,
        Long creatorId,
        String djName,
        long amount,
        SettlementStatus status,
        LocalDateTime requestedAt,
        LocalDateTime processedAt
    ) {

        public static SettlementItem of(SettlementRequest request, Creator creator) {
            String djName = creator == null ? "(삭제된 크리에이터)" : creator.getDjName();
            return new SettlementItem(
                request.getSettlementRequestId(),
                request.getCreatorId(),
                djName,
                request.getAmount(),
                request.getStatus(),
                request.getRequestedAt(),
                request.getProcessedAt()
            );
        }
    }
}

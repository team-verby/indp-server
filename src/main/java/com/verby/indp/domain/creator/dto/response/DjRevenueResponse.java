package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.settlement.SettlementRequest;
import java.time.LocalDateTime;

/**
 * DJ 정산 조회 응답. 금액·산식은 노출하지 않고 결과 금액만 내려준다.
 *
 * @param thisMonthEstimate  당월 실시간 예상 적립액 (원)
 * @param balance            현재 적립 잔액 (원)
 * @param totalPaid          누적 지급액 (원, 지급 완료된 신청 금액 합)
 * @param canRequest         정산 신청 가능 여부 (잔액 ≥ 최소 신청 금액 그리고 대기 중 신청 없음)
 * @param minPayout          최소 신청 금액 (원)
 * @param hasPendingRequest  처리 대기 중인 정산 신청 존재 여부
 * @param lastSettlement     최근 처리(지급/반려)된 정산 결과 (없으면 null)
 */
public record DjRevenueResponse(
    Long thisMonthEstimate,
    Long balance,
    Long totalPaid,
    boolean canRequest,
    Long minPayout,
    boolean hasPendingRequest,
    LastSettlement lastSettlement
) {

    /**
     * 최근 처리된 정산 결과 요약.
     *
     * @param status      처리 상태 (PAID=지급 완료 / REJECTED=반려)
     * @param amount      정산 금액 (원)
     * @param processedAt 처리 일시
     */
    public record LastSettlement(String status, Long amount, LocalDateTime processedAt) {

        public static LastSettlement of(SettlementRequest request) {
            if (request == null) {
                return null;
            }
            return new LastSettlement(
                request.getStatus().name(), request.getAmount(), request.getProcessedAt());
        }
    }
}

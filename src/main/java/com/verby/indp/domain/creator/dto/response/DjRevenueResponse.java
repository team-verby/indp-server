package com.verby.indp.domain.creator.dto.response;

/**
 * DJ 정산 조회 응답. 금액·산식은 노출하지 않고 결과 금액만 내려준다.
 *
 * @param thisMonthEstimate  당월 실시간 예상 적립액 (원)
 * @param balance            현재 적립 잔액 (원)
 * @param totalPaid          누적 지급액 (원, 정산 지급 연동 전에는 0)
 * @param canRequest         정산 신청 가능 여부 (잔액 ≥ 최소 신청 금액 그리고 대기 중 신청 없음)
 * @param minPayout          최소 신청 금액 (원)
 * @param hasPendingRequest  처리 대기 중인 정산 신청 존재 여부
 */
public record DjRevenueResponse(
    Long thisMonthEstimate,
    Long balance,
    Long totalPaid,
    boolean canRequest,
    Long minPayout,
    boolean hasPendingRequest
) {

}

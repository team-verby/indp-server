package com.verby.indp.domain.settlement;

/** 크리에이터 정산(출금) 신청 상태. */
public enum SettlementStatus {
    /** 신청됨 — 어드민 처리 대기 */
    REQUESTED,
    /** 지급 완료 */
    PAID,
    /** 반려 */
    REJECTED
}

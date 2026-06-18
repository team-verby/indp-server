package com.verby.indp.domain.settlement;

/** 크리에이터 적립금 원장 거래 유형. */
public enum LedgerType {
    /** 월 청취시간 적립 (+) */
    ACCRUAL,
    /** 정산 출금 (−) */
    PAYOUT,
    /** 조정 — 출금 거절 환원 등 */
    ADJUST
}

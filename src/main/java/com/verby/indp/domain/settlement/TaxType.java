package com.verby.indp.domain.settlement;

/** 정산 세금 처리 유형. */
public enum TaxType {
    /** 개인 — 정산액의 3.3% 원천징수 후 지급 */
    INDIVIDUAL,
    /** 사업자 — 세금계산서 발행 */
    BUSINESS
}

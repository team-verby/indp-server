package com.verby.indp.domain.listening;

/**
 * 정산 정책 상수 (단가·상한). 단가 변경 시 이 파일만 수정한다.
 * 근거: CREATOR_SETTLEMENT_POLICY.md (1.0417원/청취시간 → 3,456 청취초 = 1원).
 */
public final class SettlementPolicy {

    /** 청취 3,456초당 1원 적립 (= 1.0417원/시간). 적립액(원) = floor(총 청취초 / 3456). */
    public static final long SECONDS_PER_WON = 3456;

    /** 정산(출금) 최소 신청 금액. */
    public static final long MIN_PAYOUT_WON = 50_000;

    /** 사용자 1인당 하루 집계 상한 (18시간). 봇·무한루프 방지. */
    public static final int DAILY_CAP_SEC = 64_800;

    /** 프론트 heartbeat 전송 주기 (5분). 참고용 상수. */
    public static final int HEARTBEAT_SEC = 300;

    /** heartbeat 1건당 인정 상한 (5분 + 지터·재개 여유). 초과분은 클램프. */
    public static final int MAX_BEAT_SEC = 360;

    /** 개인 크리에이터 사업소득 원천징수율. */
    public static final double WITHHOLDING_RATE = 0.033;

    private SettlementPolicy() {
    }
}

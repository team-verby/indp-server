package com.verby.indp.domain.settlement.scheduler;

import com.verby.indp.domain.settlement.service.CreatorAccrualService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorAccrualScheduler {

    private final CreatorAccrualService creatorAccrualService;

    /** 매월 10일 03:00 (Asia/Seoul) — 전월분 확정 적립. 멱등하므로 재실행 안전. */
    @Scheduled(cron = "0 0 3 10 * *", zone = "Asia/Seoul")
    public void accruePreviousMonth() {
        creatorAccrualService.accruePreviousMonth();
    }
}

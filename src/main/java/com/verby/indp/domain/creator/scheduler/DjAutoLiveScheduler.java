package com.verby.indp.domain.creator.scheduler;

import com.verby.indp.domain.creator.service.DjLiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 시드 DJ 계정의 라이브 상태를 라이브 창에 맞춰 자동 동기화한다.
 * 60초 주기로 heartbeat를 갱신해 라이브 TTL(150초) 안에 충분한 여유를 둔다.
 */
@Component
@RequiredArgsConstructor
public class DjAutoLiveScheduler {

    private final DjLiveService djLiveService;

    @Scheduled(fixedDelay = 60_000L)
    public void syncAutoLive() {
        djLiveService.syncAutoLive();
    }
}

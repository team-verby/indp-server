package com.verby.indp.domain.playlist.scheduler;

import com.verby.indp.domain.playlist.service.PlaylistService;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistScheduler {

    private final PlaylistService playlistService;
    private final Clock clock;

    // 매분 검사 — 관리자가 지정한 시·분에 정확히 반영되도록(30분 단위 제약 제거)
    @Scheduled(cron = "0 * * * * *")
    public void applyScheduledPlaylistUpdates() {
        playlistService.applyDueScheduledUpdates();
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void deleteRecommendedSongsAtClose() {
        LocalDateTime now = LocalDateTime.now(clock);
        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalTime closeTime = now.toLocalTime().withSecond(0).withNano(0);
        LocalTime fromCloseTime = closeTime.minusMinutes(30);

        playlistService.deleteRecommendedSongsOfClosingStores(dayOfWeek, fromCloseTime, closeTime);
    }
}

package com.verby.indp.domain.playlist.scheduler;

import com.verby.indp.domain.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class PlaylistScheduler {

    private final PlaylistService playlistService;

    @Scheduled(cron = "0 0/30 * * * *")
    public void applyScheduledPlaylistUpdates() {
        playlistService.applyDueScheduledUpdates();
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void deleteRecommendedSongsAtClose() {
        LocalDateTime now = LocalDateTime.now();
        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalTime closeTime = now.toLocalTime().withSecond(0).withNano(0);
        LocalTime fromCloseTime = closeTime.minusMinutes(30);

        playlistService.deleteRecommendedSongsOfClosingStores(dayOfWeek, fromCloseTime, closeTime);
    }
}

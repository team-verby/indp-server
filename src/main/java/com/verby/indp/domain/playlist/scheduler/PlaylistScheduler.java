package com.verby.indp.domain.playlist.scheduler;

import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.repository.StoreBusinessHourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaylistScheduler {

    private final StoreBusinessHourRepository storeBusinessHourRepository;
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

        List<StoreBusinessHour> closingStores = storeBusinessHourRepository
            .findByDayOfWeekAndCloseTimeBetween(dayOfWeek, fromCloseTime, closeTime);

        for (StoreBusinessHour businessHour : closingStores) {
            playlistService.deleteRecommendedSongs(businessHour.getStore());
        }
    }
}

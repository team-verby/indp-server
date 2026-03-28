package com.verby.indp.domain.playlist.scheduler;

import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.repository.StoreBusinessHourRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaylistScheduler {

    private final StoreBusinessHourRepository storeBusinessHourRepository;
    private final PlaylistService playlistService;

    @Scheduled(cron = "0 * * * * *")
    public void applyScheduledPlaylistUpdates() {
        playlistService.applyDueScheduledUpdates();
    }

    @Scheduled(cron = "0 * * * * *")
    public void deleteRecommendedSongsAtClose() {
        LocalDateTime now = LocalDateTime.now();
        int dayOfWeek = now.getDayOfWeek().getValue();
        LocalTime closeTime = now.toLocalTime().withSecond(0).withNano(0);

        List<StoreBusinessHour> closingStores = storeBusinessHourRepository
            .findByDayOfWeekAndCloseTime(dayOfWeek, closeTime);

        for (StoreBusinessHour businessHour : closingStores) {
            playlistService.deleteRecommendedSongs(businessHour.getStore());
        }
    }
}

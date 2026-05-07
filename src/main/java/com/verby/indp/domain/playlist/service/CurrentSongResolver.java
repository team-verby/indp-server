package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrentSongResolver {

    private static final int PRE_OPEN_MINUTES = 30;

    private final Clock clock;

    public Optional<CurrentSong> resolveCurrentSong(Store store) {
        if (store.getPlaylist() == null) {
            return Optional.empty();
        }
        long elapsedSeconds = calcElapsedSeconds(store);
        if (elapsedSeconds < 0) {
            return Optional.empty();
        }

        long cumulative = 0;
        for (PlaylistSong song : store.getPlaylist().getSongs()) {
            long duration = song.getPlayTime();
            if (cumulative + duration > elapsedSeconds) {
                return Optional.of(
                    new CurrentSong(song.getPlaylistSongId(), song.getTitle(), song.getArtist(),
                        song.getVid(), (int) (elapsedSeconds - cumulative)));
            }

            cumulative += duration;
        }

        return Optional.empty();
    }

    private long calcElapsedSeconds(Store store) {
        LocalDateTime now = LocalDateTime.now(clock);
        int todayDayOfWeek = now.getDayOfWeek().getValue();
        LocalTime nowTime = now.toLocalTime();

        long todayElapsed = calcTodayElapsed(store, todayDayOfWeek, nowTime);
        if (todayElapsed >= 0) {
            return todayElapsed;
        }

        return calcYesterdayOverflowElapsed(store, todayDayOfWeek, nowTime);
    }

    private long calcTodayElapsed(Store store, int dayOfWeek, LocalTime nowTime) {
        Optional<StoreBusinessHour> todayHour = findBusinessHour(store, dayOfWeek);
        if (todayHour.isEmpty()) {
            return -1;
        }

        LocalTime startTime = todayHour.get().getOpenTime().minusMinutes(PRE_OPEN_MINUTES);
        if (nowTime.isBefore(startTime)) {
            return -1;
        }

        return java.time.Duration.between(startTime, nowTime).getSeconds();
    }

    private long calcYesterdayOverflowElapsed(Store store, int todayDayOfWeek, LocalTime nowTime) {
        int yesterdayDayOfWeek = todayDayOfWeek == 1 ? 7 : todayDayOfWeek - 1;
        Optional<StoreBusinessHour> yesterdayHour = findBusinessHour(store, yesterdayDayOfWeek);
        if (yesterdayHour.isEmpty()) {
            return -1;
        }

        StoreBusinessHour yh = yesterdayHour.get();
        if (!crossesMidnight(yh) || !nowTime.isBefore(yh.getCloseTime())) {
            return -1;
        }

        LocalTime startTime = yh.getOpenTime().minusMinutes(PRE_OPEN_MINUTES);
        long secondsBeforeMidnight = java.time.Duration.between(startTime, LocalTime.MAX).getSeconds() + 1;
        long secondsAfterMidnight = java.time.Duration.between(LocalTime.MIDNIGHT, nowTime).getSeconds();
        return secondsBeforeMidnight + secondsAfterMidnight;
    }

    private Optional<StoreBusinessHour> findBusinessHour(Store store, int dayOfWeek) {
        return store.getBusinessHours().stream()
            .filter(h -> h.getDayOfWeek() == dayOfWeek && !h.isClosed())
            .findFirst();
    }

    private boolean crossesMidnight(StoreBusinessHour hour) {
        return hour.getCloseTime().isBefore(hour.getOpenTime())
            || hour.getCloseTime().equals(LocalTime.MIDNIGHT);
    }

}

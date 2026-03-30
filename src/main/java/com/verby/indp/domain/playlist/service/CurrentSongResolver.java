package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSongResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class CurrentSongResolver {

    public CurrentSongResponse resolveCurrentSong(Store store, List<PlaylistSong> songs) {
        long elapsedSeconds = calcElapsedSeconds(store);
        if (elapsedSeconds < 0) {
            return null;
        }

        long cumulative = 0;
        for (PlaylistSong song : songs) {
            long duration = song.getPlayTime();
            if (cumulative + duration > elapsedSeconds) {
                return new CurrentSongResponse(song.getPlaylistSongId(), (int) (elapsedSeconds - cumulative));
            }
            cumulative += duration;
        }

        return null;
    }

    public long calcElapsedSeconds(Store store) {
        LocalDateTime now = LocalDateTime.now();
        int todayDayOfWeek = now.getDayOfWeek().getValue();

        Optional<StoreBusinessHour> todayHour = store.getBusinessHours().stream()
            .filter(h -> h.getDayOfWeek() == todayDayOfWeek && !h.isClosed())
            .findFirst();

        if (todayHour.isEmpty()) {
            return -1;
        }

        LocalTime startTime = todayHour.get().getOpenTime().minusMinutes(30);
        LocalTime nowTime = now.toLocalTime();

        if (nowTime.isBefore(startTime)) {
            return -1;
        }

        return java.time.Duration.between(startTime, nowTime).getSeconds();
    }

}

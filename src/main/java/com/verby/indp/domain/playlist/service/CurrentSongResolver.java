package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentSongResolver {

    public static Optional<CurrentSong> resolveCurrentSong(Store store) {
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

    public static long calcElapsedSeconds(Store store) {
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

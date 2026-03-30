package com.verby.indp.domain.playlist.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public record SchedulePlaylistsUpdateRequest(
        List<SchedulePlaylistItem> schedulePlaylists
) {
    public record SchedulePlaylistItem(
            Long storeId,
            List<SongItem> songs,
            LocalDateTime scheduledAt
    ) {
        public record SongItem(
                String title,
                String artist,
                String vid,
                Integer playTime,
                Double playOrder
        ) {}
    }
}

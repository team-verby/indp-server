package com.verby.indp.domain.playlist.dto.response;

import java.util.List;

public record FindStorePlaylistByOwnerResponse(
    CurrentSongItem currentSong,
    PlaylistInfo playlist
) {

    public record CurrentSongItem(
        Long playlistSongId,
        double playOrder,
        String title,
        String artist,
        String vid,
        Integer playTime,
        int elapsedSeconds,
        boolean isRecommended,
        String refereeName
    ) {
    }

    public record PlaylistInfo(
        int totalCount,
        int recommendedCount,
        int totalPlayTime,
        List<SongItem> songs
    ) {
    }

    public record SongItem(
        Long playlistSongId,
        double playOrder,
        String title,
        String artist,
        Integer playTime,
        boolean isRecommended,
        String refereeName
    ) {
    }
}

package com.verby.indp.domain.playlist.dto.request;

import java.util.List;

public record UpdateMusicCatalogRequest(
    List<MoodCatalog> moods
) {

    public record MoodCatalog(
        String mood,
        List<SongItem> songs
    ) {

    }

    public record SongItem(
        String title,
        String artist,
        String playTime,
        String vid
    ) {

    }
}

package com.verby.indp.domain.playlist.dto.response;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record FindMusicCatalogResponse(
    LocalDateTime savedAt,
    List<MoodCatalog> moods
) {

    public static FindMusicCatalogResponse from(LocalDateTime savedAt,
        List<MusicCatalogSong> songs) {
        Map<String, List<SongItem>> grouped = new LinkedHashMap<>();
        for (MusicCatalogSong song : songs) {
            grouped.computeIfAbsent(song.getMood(), key -> new ArrayList<>())
                .add(new SongItem(song.getTitle(), song.getArtist(), song.getPlayTime(),
                    song.getVid()));
        }
        List<MoodCatalog> moods = grouped.entrySet().stream()
            .map(entry -> new MoodCatalog(entry.getKey(), entry.getValue()))
            .toList();
        return new FindMusicCatalogResponse(savedAt, moods);
    }

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

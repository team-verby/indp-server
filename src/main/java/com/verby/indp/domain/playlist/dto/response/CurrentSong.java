package com.verby.indp.domain.playlist.dto.response;

public record CurrentSong(
    long playlistSongId,
    String title,
    String artist,
    String vid,
    int elapsedSeconds
) {
}


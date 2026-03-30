package com.verby.indp.domain.playlist.dto.response;

public record CurrentSongResponse(
    Long playlistSongId,
    int elapsedSeconds
) {
}

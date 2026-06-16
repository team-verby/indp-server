package com.verby.indp.domain.playlist.dto.request;

import java.time.LocalDate;

public record CreateFixedPlaylistSongRequest(
    String storeName,
    LocalDate startDate,
    LocalDate endDate,
    Integer hour,
    Integer position,
    String title,
    String artist,
    String vid,
    Integer playTime
) {

}

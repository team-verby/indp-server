package com.verby.indp.domain.playlist.dto.response;

import com.verby.indp.domain.playlist.FixedPlaylistSong;

import java.time.LocalDate;
import java.util.List;

public record FindFixedPlaylistSongsResponse(
    List<FixedPlaylistSongItem> fixedSongs
) {

    public static FindFixedPlaylistSongsResponse from(List<FixedPlaylistSong> songs) {
        return new FindFixedPlaylistSongsResponse(
            songs.stream().map(FixedPlaylistSongItem::from).toList());
    }

    public record FixedPlaylistSongItem(
        Long id,
        String storeName,
        LocalDate startDate,
        LocalDate endDate,
        Integer hour,
        int position,
        String title,
        String artist,
        String vid,
        Integer playTime
    ) {

        public static FixedPlaylistSongItem from(FixedPlaylistSong song) {
            return new FixedPlaylistSongItem(
                song.getFixedPlaylistSongId(),
                song.getStore().getName(),
                song.getStartDate(),
                song.getEndDate(),
                song.getTargetHour(),
                song.getPosition(),
                song.getTitle(),
                song.getArtist(),
                song.getVid(),
                song.getPlayTime()
            );
        }
    }
}

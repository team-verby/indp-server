package com.verby.indp.domain.playlist.dto.response;

import com.verby.indp.domain.playlist.PlaylistSong;

import java.util.List;

public record FindStorePlaylistResponse(
    CurrentSongItem currentSong,
    PlaylistInfo playlist
) {

    public static FindStorePlaylistResponse from(List<PlaylistSong> songs, PlaylistSong currentSong) {
        CurrentSongItem currentSongItem = CurrentSongItem.from(currentSong);
        PlaylistInfo playlistInfo = PlaylistInfo.from(songs);
        return new FindStorePlaylistResponse();
    }

    public record CurrentSongItem(
        Long playlistSongId,
        double playOrder,
        String title,
        String artist,
        Integer playTime,
        int elapsedSeconds,
        boolean isRecommended,
        String refereeName
    ) {
        public static CurrentSongItem from(PlaylistSong currentSong) {

        }
    }

    public record PlaylistInfo(
        int totalCount,
        int recommendedCount,
        int totalPlayTime,
        List<SongItem> songs
    ) {

        public static PlaylistInfo from(List<PlaylistSong> songs) {
            int totalCount = songs.size();
            int recommendedCount = (int) songs.stream().filter(PlaylistSong::isRecommended).count();
            int totalPlayTime = songs.stream().mapToInt(s -> s.getPlayTime() != null ? s.getPlayTime() : 0).sum();
            List<SongItem> songItems = songs.stream().map(SongItem::from).toList();

            return new PlaylistInfo(totalCount, recommendedCount, totalPlayTime, songItems);
        }
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

        private static SongItem from(PlaylistSong song) {
            return new SongItem(song.getPlaylistSongId(), song.getPlayOrder(), song.getTitle(), song.getArtist(),
                    song.getPlayTime(), song.isRecommended(), song.isRecommended() ? song.getSongRecommendation().getRefereeName() : null);
        }
    }
}

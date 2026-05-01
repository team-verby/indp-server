package com.verby.indp.domain.playlist.dto.response;

import com.verby.indp.domain.playlist.PlaylistSong;

import java.util.List;

public record FindStorePlaylistByOwnerResponse(
    PlaylistInfo playlist,
    CurrentSongItem currentSong
) {

    public static FindStorePlaylistByOwnerResponse from(List<PlaylistSong> songs, CurrentSong currentSong) {
        PlaylistInfo playlistInfo = PlaylistInfo.from(songs);
        CurrentSongItem song = currentSong != null ? CurrentSongItem.from(currentSong) : null;
        return new FindStorePlaylistByOwnerResponse(playlistInfo, song);
    }

    private record PlaylistInfo(
        int totalCount,
        int recommendedCount,
        int totalPlayTime,
        List<SongItem> songs
    ) {

        private static PlaylistInfo from(List<PlaylistSong> songs) {
            int totalCount = songs.size();
            int recommendedCount = (int) songs.stream().filter(PlaylistSong::isRecommended).count();
            int totalPlayTime = songs.stream()
                .mapToInt(s -> s.getPlayTime() != null ? s.getPlayTime() : 0).sum();
            List<SongItem> songItems = songs.stream().map(SongItem::from).toList();

            return new PlaylistInfo(totalCount, recommendedCount, totalPlayTime, songItems);
        }
    }

    private record SongItem(
        Long playlistSongId,
        double playOrder,
        String title,
        String artist,
        Integer playTime,
        boolean isRecommended,
        String refereeName,
        String vid
    ) {

        private static SongItem from(PlaylistSong song) {
            return new SongItem(song.getPlaylistSongId(), song.getPlayOrder(), song.getTitle(),
                song.getArtist(),
                song.getPlayTime(), song.isRecommended(),
                song.isRecommended() ? song.getSongRecommendation().getRefereeName() : null,
                song.getVid());
        }
    }

    private record CurrentSongItem(long playlistSongId, String title, String artist, String vid,
                                   int elapsedSeconds) {

        private static CurrentSongItem from(CurrentSong song) {
            return new CurrentSongItem(song.playlistSongId(), song.title(), song.artist(),
                song.vid(), song.elapsedSeconds());
        }
    }
}

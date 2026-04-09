package com.verby.indp.fixture;

import com.verby.indp.domain.playlist.PlaylistSong;
import org.springframework.test.util.ReflectionTestUtils;

public class PlaylistSongFixture {

    public static PlaylistSong playlistSong() {
        return playlistSongWithId(1L, 1.0);
    }

    public static PlaylistSong playlistSongWithId(Long id, double playOrder) {
        PlaylistSong song = new PlaylistSong(null, false, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "성시경",
            playOrder);
        ReflectionTestUtils.setField(song, "playlistSongId", id);
        return song;
    }

    public static PlaylistSong recommendedSong() {
        return new PlaylistSong(null, true, "5zAEiu3SaO4", 259, "안녕 나의 사랑", "성시경", 1.0);
    }

    public static PlaylistSong normalSong(String vid, String title, String artist, int playTime,
        double playOrder) {
        return new PlaylistSong(null, false, vid, playTime, title, artist, playOrder);
    }
}

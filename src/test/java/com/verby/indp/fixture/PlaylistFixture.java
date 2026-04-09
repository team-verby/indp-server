package com.verby.indp.fixture;

import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import java.util.ArrayList;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class PlaylistFixture {

    public static Playlist playlist() {
        PlaylistSong song = PlaylistSongFixture.playlistSong();
        Playlist playlist = new Playlist(new ArrayList<>(List.of(song)));
        ReflectionTestUtils.setField(playlist, "playlistId", 1L);
        return playlist;
    }

    public static Playlist playlistWithSongs(List<PlaylistSong> songs) {
        Playlist playlist = new Playlist(new ArrayList<>(songs));
        ReflectionTestUtils.setField(playlist, "playlistId", 1L);
        return playlist;
    }
}

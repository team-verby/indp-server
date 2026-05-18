package com.verby.indp.fixture;

import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.store.Store;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class ScheduledPlaylistFixture {

    public static ScheduledPlaylist scheduledPlaylist() {
        Store store = StoreFixture.store();
        ScheduledPlaylistSong song = new ScheduledPlaylistSong("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259,
            10.0);
        ScheduledPlaylist playlist = new ScheduledPlaylist(store,
            LocalDateTime.now().plusHours(1), List.of(song));
        ReflectionTestUtils.setField(playlist, "scheduledPlaylistId", 1L);
        return playlist;
    }

    public static ScheduledPlaylist scheduledPlaylistWithSongs(List<ScheduledPlaylistSong> songs) {
        Store store = StoreFixture.store();
        ScheduledPlaylist playlist = new ScheduledPlaylist(store,
            LocalDateTime.now().plusHours(1), songs);
        ReflectionTestUtils.setField(playlist, "scheduledPlaylistId", 1L);
        return playlist;
    }
}

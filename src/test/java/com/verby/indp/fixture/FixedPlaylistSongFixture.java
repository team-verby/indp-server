package com.verby.indp.fixture;

import com.verby.indp.domain.playlist.FixedPlaylistSong;
import com.verby.indp.domain.store.Store;
import java.time.LocalDate;
import org.springframework.test.util.ReflectionTestUtils;

public class FixedPlaylistSongFixture {

    public static FixedPlaylistSong fixedPlaylistSong() {
        Store store = StoreFixture.store();
        FixedPlaylistSong song = new FixedPlaylistSong(
            store,
            LocalDate.of(2026, 6, 1),
            LocalDate.of(2026, 6, 30),
            14,
            3,
            "안녕 나의 사랑",
            "성시경",
            "5zAEiu3SaO4",
            259
        );
        ReflectionTestUtils.setField(song, "fixedPlaylistSongId", 1L);
        return song;
    }
}

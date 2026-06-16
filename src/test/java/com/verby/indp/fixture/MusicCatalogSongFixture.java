package com.verby.indp.fixture;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import org.springframework.test.util.ReflectionTestUtils;

public class MusicCatalogSongFixture {

    public static MusicCatalogSong musicCatalogSong() {
        MusicCatalogSong song = new MusicCatalogSong(
            "잔잔한",
            1,
            "안녕 나의 사랑",
            "성시경",
            "4:19",
            "5zAEiu3SaO4"
        );
        ReflectionTestUtils.setField(song, "musicCatalogSongId", 1L);
        return song;
    }
}

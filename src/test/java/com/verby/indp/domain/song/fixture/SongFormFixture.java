package com.verby.indp.domain.song.fixture;

import com.verby.indp.domain.song.SongForm;

public class SongFormFixture {

    private static final String SONG_FORM_NAME = "SongFormName";

    public static SongForm songForm() {
        return new SongForm(SONG_FORM_NAME);
    }

}

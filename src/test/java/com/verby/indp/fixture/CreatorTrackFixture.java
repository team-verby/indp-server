package com.verby.indp.fixture;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import org.springframework.test.util.ReflectionTestUtils;

public class CreatorTrackFixture {

    public static CreatorTrack track(Creator creator) {
        return new CreatorTrack(creator, "morning_haze.mp3",
            "https://cdn.example.com/tracks/morning_haze.mp3", "3:42", 222);
    }

    public static CreatorTrack trackWithId(Creator creator, Long id) {
        CreatorTrack track = track(creator);
        ReflectionTestUtils.setField(track, "trackId", id);
        return track;
    }
}

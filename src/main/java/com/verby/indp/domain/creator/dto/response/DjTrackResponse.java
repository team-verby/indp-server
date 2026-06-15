package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.CreatorTrack;

public record DjTrackResponse(
    Long id,
    String filename,
    String duration,
    int secs,
    String streamUrl
) {

    public static DjTrackResponse from(CreatorTrack track) {
        return new DjTrackResponse(
            track.getTrackId(),
            track.getFilename(),
            track.getDuration(),
            track.getSecs(),
            track.getStreamUrl()
        );
    }
}

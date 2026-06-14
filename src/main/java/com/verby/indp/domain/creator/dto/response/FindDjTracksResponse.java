package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.CreatorTrack;

import java.util.List;

public record FindDjTracksResponse(
    List<DjTrackResponse> tracks
) {

    public static FindDjTracksResponse from(List<CreatorTrack> tracks) {
        return new FindDjTracksResponse(
            tracks.stream().map(DjTrackResponse::from).toList()
        );
    }
}

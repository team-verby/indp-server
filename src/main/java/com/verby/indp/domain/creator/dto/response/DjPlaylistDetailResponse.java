package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;

import java.util.List;

public record DjPlaylistDetailResponse(
    Long id,
    String name,
    String djName,
    String thumbnailUrl,
    String introduction,
    boolean isLive,
    int listeners,
    int currentIdx,
    List<DjTrackResponse> tracks
) {

    public static DjPlaylistDetailResponse from(Creator creator, List<CreatorTrack> tracks) {
        return new DjPlaylistDetailResponse(
            creator.getCreatorId(),
            creator.getDjName() + " 채널",
            creator.getDjName(),
            creator.getThumbnailUrl(),
            creator.getIntroduction(),
            creator.isLive(),
            0,
            0,
            tracks.stream().map(DjTrackResponse::from).toList()
        );
    }
}

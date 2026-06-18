package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.Creator;

import java.util.List;

public record FindDjPlaylistsResponse(
    List<DjPlaylistItem> playlists
) {

    public record DjPlaylistItem(
        Long id,
        String name,
        String djName,
        String thumbnailUrl,
        boolean isLive
    ) {

        public static DjPlaylistItem from(Creator creator) {
            return new DjPlaylistItem(
                creator.getCreatorId(),
                creator.getDjName() + " 채널",
                creator.getDjName(),
                creator.getThumbnailUrl(),
                creator.isLive()
            );
        }
    }
}

package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.Creator;

public record DjProfileResponse(
    String djName,
    String name,
    String phone,
    String email,
    String thumbnailUrl,
    String introduction
) {

    public static DjProfileResponse from(Creator creator) {
        return new DjProfileResponse(
            creator.getDjName(),
            creator.getName(),
            creator.getPhone(),
            creator.getEmail(),
            creator.getThumbnailUrl(),
            creator.getIntroduction()
        );
    }
}

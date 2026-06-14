package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.Creator;

import java.time.LocalDateTime;
import java.util.List;

public record FindCreatorsResponse(
    List<CreatorItem> creators
) {

    public record CreatorItem(
        Long id,
        String name,
        String djName,
        String phone,
        String email,
        String thumbnailUrl,
        LocalDateTime createdAt,
        boolean active,
        boolean isLive,
        int listenerCount,
        int trackCount,
        Long totalListenMinutes,
        Long subscriberCount,
        Long thisMonthEstimate,
        Long totalPaid
    ) {

        public static CreatorItem from(Creator creator) {
            return new CreatorItem(
                creator.getCreatorId(),
                creator.getName(),
                creator.getDjName(),
                creator.getPhone(),
                creator.getEmail(),
                creator.getThumbnailUrl(),
                creator.getCreatedAt(),
                creator.isActive(),
                false,
                0,
                0,
                null,
                null,
                null,
                null
            );
        }
    }
}

package com.verby.indp.domain.creator.dto.response;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.listening.SettlementPolicy;

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
        Long totalPaid,
        // 어드민 전용 — 누적 청취 기반 정산 적립액. 1원 단위로 내림하지 않은 정확한 소수점 금액.
        Long totalListenSeconds,
        Double accruedWon
    ) {

        public static CreatorItem from(Creator creator) {
            return from(creator, 0L);
        }

        public static CreatorItem from(Creator creator, long totalListenSeconds) {
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
                null,
                totalListenSeconds,
                (double) totalListenSeconds / SettlementPolicy.SECONDS_PER_WON
            );
        }
    }
}

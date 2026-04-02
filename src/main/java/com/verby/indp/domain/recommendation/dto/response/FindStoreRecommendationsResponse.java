package com.verby.indp.domain.recommendation.dto.response;

import com.verby.indp.domain.recommendation.SongRecommendation;

import java.time.LocalDateTime;
import java.util.List;

public record FindStoreRecommendationsResponse(
    List<RecommendationItem> recommendations
) {
    public static FindStoreRecommendationsResponse from(List<SongRecommendation> recommendations) {
        return new FindStoreRecommendationsResponse(
            recommendations.stream()
                .map(RecommendationItem::from)
                .toList()
        );
    }

    private record RecommendationItem(
        Long songRecommendationId,
        String title,
        String artist,
        String vid,
        Integer playTime,
        String refereeName,
        LocalDateTime recommendedAt
    ) {
        private static RecommendationItem from(SongRecommendation recommendation) {
            return new RecommendationItem(
                recommendation.getSongRecommendationId(),
                recommendation.getTitle(),
                recommendation.getArtist(),
                recommendation.getVid(),
                recommendation.getPlayTime(),
                recommendation.getRefereeName(),
                recommendation.getCreatedAt()
            );
        }
    }
}

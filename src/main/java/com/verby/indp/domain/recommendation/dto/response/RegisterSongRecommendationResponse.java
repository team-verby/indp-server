package com.verby.indp.domain.recommendation.dto.response;

import com.verby.indp.domain.recommendation.SongRecommendation;

public record RegisterSongRecommendationResponse(
    Long songRecommendationId,
    String title,
    String artist,
    int fee,
    String orderId,
    int amount,
    String orderName
) {
    public static RegisterSongRecommendationResponse from(SongRecommendation recommendation) {
        return new RegisterSongRecommendationResponse(
            recommendation.getSongRecommendationId(),
            recommendation.getTitle(),
            recommendation.getArtist(),
            recommendation.getFee(),
            recommendation.getPayment().getOrderId(),
            recommendation.getPayment().getAmount(),
            recommendation.getPayment().getOrderName()
        );
    }
}

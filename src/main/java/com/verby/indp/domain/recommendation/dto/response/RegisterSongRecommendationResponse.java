package com.verby.indp.domain.recommendation.dto.response;

import com.verby.indp.domain.recommendation.SongRecommendation;

public record RegisterSongRecommendationResponse(
    Long songRecommendationId,
    String orderId,
    int amount,
    String orderName
) {

    public static RegisterSongRecommendationResponse from(SongRecommendation recommendation) {
        return new RegisterSongRecommendationResponse(
            recommendation.getSongRecommendationId(),
            recommendation.getPayment().getOrderId(),
            recommendation.getPayment().getTotalAmount(),
            recommendation.getPayment().getOrderName()
        );
    }
}

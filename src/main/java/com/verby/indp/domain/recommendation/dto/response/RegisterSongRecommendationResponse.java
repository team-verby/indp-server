package com.verby.indp.domain.recommendation.dto.response;

import com.verby.indp.domain.recommendation.SongRecommendation;

public record RegisterSongRecommendationResponse(
    String orderId,
    int amount,
    String orderName
) {

    public static RegisterSongRecommendationResponse from(SongRecommendation recommendation) {
        return new RegisterSongRecommendationResponse(
            recommendation.getPayment().getOrderId(),
            recommendation.getPayment().getAmount(),
            recommendation.getPayment().getOrderName()
        );
    }
}

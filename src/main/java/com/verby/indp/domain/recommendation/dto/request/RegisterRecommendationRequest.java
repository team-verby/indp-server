package com.verby.indp.domain.recommendation.dto.request;

public record RegisterRecommendationRequest(
    Long storeId,
    String information,
    String phoneNumber
) {

}

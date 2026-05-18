package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.policy.PricePolicy;

public record FindRecommendationFeeResponse(int amount) {

    public static FindRecommendationFeeResponse from(PricePolicy pricePolicy) {
        return new FindRecommendationFeeResponse(pricePolicy.getAmount());
    }
}

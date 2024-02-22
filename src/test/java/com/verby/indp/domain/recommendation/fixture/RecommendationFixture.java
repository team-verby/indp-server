package com.verby.indp.domain.recommendation.fixture;

import com.verby.indp.domain.recommendation.Recommendation;
import com.verby.indp.domain.store.Store;

public class RecommendationFixture {

    private static final String RECOMMENDATION_INFORMATION = "recommendationInformation";
    private static final String PHONE_NUMBER = "01012345678";

    public static Recommendation recommendation(Store store) {
        return new Recommendation(store, RECOMMENDATION_INFORMATION, PHONE_NUMBER);
    }

}

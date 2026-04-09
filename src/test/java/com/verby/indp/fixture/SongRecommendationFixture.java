package com.verby.indp.fixture;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;

public class SongRecommendationFixture {

    public static SongRecommendation songRecommendation() {
        Store store = StoreFixture.store();
        Payment payment = PaymentFixture.payment("인디피_노래추천_카페 공명 홍대점", 3000);
        return new SongRecommendation(store, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, "홍길동",
            payment);
    }

    public static SongRecommendation songRecommendation(Store store) {
        Payment payment = PaymentFixture.payment("인디피_노래추천_카페 공명 홍대점", 3000);
        return new SongRecommendation(store, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259, "홍길동",
            payment);
    }
}

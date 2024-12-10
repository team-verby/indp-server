package com.verby.indp.acceptance;

import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.acceptance.support.RecommendationSupporter;
import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("recommendation 인수 테스트")
class RecommendationAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    @DisplayName("추천 음악 정보를 등록한다.")
    void registerRecommendation() {
        // given
        Region 서울 = region("서울");
        regionRepository.save(서울);

        Store store = store(서울);
        storeRepository.save(store);

        String information = "아이유-밤편지";
        String phoneNumber = "01012345678";

        RegisterRecommendationRequest request = new RegisterRecommendationRequest(
            store.getStoreId(), information, phoneNumber);

        // when
        ExtractableResponse<Response> result = RecommendationSupporter.registerRecommendation(
            request);

        // then
        assertThat(result.statusCode()).isEqualTo(201);
        assertThat(result.header("Location")).isNotBlank();
    }

}

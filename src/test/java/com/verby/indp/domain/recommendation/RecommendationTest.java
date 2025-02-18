package com.verby.indp.domain.recommendation;

import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.store.Store;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecommendationTest {

    @Nested
    @DisplayName("Recommendation 생성 시")
    class NewRecommendation {

        @Test
        @DisplayName("성공: Recommendation 을 생성한다.")
        void newRecommendation() {
            // given
            Region 서울 = region("서울");
            Store store = store(서울, List.of(), List.of());
            String information = "공차";
            String phoneNumber = "01012345678";

            // when
            Exception exception = catchException(
                () -> new Recommendation(store, information, phoneNumber));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외: Store 가 null 이라면 예외가 발생한다.")
        void exceptionWhenStoreIsNull() {
            // given
            Store nullStore = null;
            String information = "공차";
            String phoneNumber = "01012345678";

            // when
            Exception exception = catchException(
                () -> new Recommendation(nullStore, information, phoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

}

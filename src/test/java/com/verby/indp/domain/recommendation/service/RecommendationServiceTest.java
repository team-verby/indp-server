package com.verby.indp.domain.recommendation.service;

import static com.verby.indp.domain.recommendation.fixture.RecommendationFixture.recommendation;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.recommendation.Recommendation;
import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.recommendation.repository.RecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @InjectMocks
    private RecommendationService recommendationService;

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("registerRecommendation 메소드 실행 시")
    class RegisterRecommendation {

        @Test
        @DisplayName("성공: 추천 음악 정보를 저장한다.")
        void registerRecommendation() {
            // given
            long storeId = 1L;
            Store store = store();
            Recommendation recommendation = recommendation(store);

            RegisterRecommendationRequest request = new RegisterRecommendationRequest(storeId,
                recommendation.getInformation(), recommendation.getPhoneNumber());

            when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
            when(recommendationRepository.save(any(Recommendation.class))).thenReturn(
                recommendation);

            // when
            recommendationService.registerRecommendation(request);

            // then
            verify(recommendationRepository, times(1)).save(any(Recommendation.class));
        }

        @Test
        @DisplayName("예외: 매장이 존재하지 않을 경우 예외가 발생한다.")
        void exceptionWhenStoreIsNotExist() {
            // given
            long storeId = 1L;
            String information = "추천 정보";
            String phoneNumber = "01012345678";

            RegisterRecommendationRequest request = new RegisterRecommendationRequest(storeId,
                information, phoneNumber);

            when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(
                () -> recommendationService.registerRecommendation(request));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

    }

}

package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.recommendation.Recommendation;
import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.recommendation.repository.RecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public long registerRecommendation(RegisterRecommendationRequest request) {
        Store store = getStore(request);
        Recommendation recommendation = new Recommendation(store, request.information(),
            request.phoneNumber());

        Recommendation persistRecommendation = recommendationRepository.save(recommendation);

        return persistRecommendation.getRecommendationId();
    }

    private Store getStore(RegisterRecommendationRequest request) {
        return storeRepository.findById(request.storeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
    }

}

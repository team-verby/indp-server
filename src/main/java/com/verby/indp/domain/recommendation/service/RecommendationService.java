package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.recommendation.event.RecommendationMailEvent;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.notification.dto.RecommendationMail;
import com.verby.indp.domain.recommendation.Recommendation;
import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.recommendation.repository.RecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    @Value("${spring.mail.username}")
    private String to;

    private final RecommendationRepository recommendationRepository;
    private final StoreRepository storeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public long registerRecommendation(RegisterRecommendationRequest request) {
        Store store = getStore(request);

        Recommendation recommendation = new Recommendation(store, request.information(),
            request.phoneNumber());
        Recommendation persistRecommendation = recommendationRepository.save(recommendation);

        RecommendationMail recommendationMail = RecommendationMail.of(to,
            request.information(), request.phoneNumber(), store.getName(), store.getAddress());
        applicationEventPublisher.publishEvent(new RecommendationMailEvent(recommendationMail));

        return persistRecommendation.getRecommendationId();
    }

    private Store getStore(RegisterRecommendationRequest request) {
        return storeRepository.findById(request.storeId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }

}

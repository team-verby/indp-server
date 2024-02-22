package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.common.event.MailSendEvent;
import com.verby.indp.domain.notification.dto.Mail;
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

        Mail mail = new Mail(to, "[버비] 인디피 서비스에 음악이 추천되었어요!",
            "추천 음악 정보: " + request.information() + "\n" +
                "추천인 연락처: " + request.phoneNumber() + "\n" +
                "매장 이름: " + store.getName() + "\n" +
                "매장 주소: " + store.getAddress() + "\n");
        applicationEventPublisher.publishEvent(new MailSendEvent(mail));

        return persistRecommendation.getRecommendationId();
    }

    private Store getStore(RegisterRecommendationRequest request) {
        return storeRepository.findById(request.storeId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
    }

}

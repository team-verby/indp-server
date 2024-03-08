package com.verby.indp.domain.notification.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.verby.indp.domain.recommendation.event.RecommendationMailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class RecommendationMailListener {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleRecommendationMailEvent(RecommendationMailEvent event) {
        notificationService.sendRecommendationMail(event.request());
    }

}

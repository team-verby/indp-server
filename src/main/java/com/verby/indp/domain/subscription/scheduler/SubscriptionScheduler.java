package com.verby.indp.domain.subscription.scheduler;

import com.verby.indp.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * *")
    public void activateSubscriptions() {
        subscriptionService.activateSubscriptions();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void expireSubscriptions() {
        subscriptionService.expireSubscriptions();
    }
}

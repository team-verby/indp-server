package com.verby.indp.domain.subscription.scheduler;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.subscription.service.SubscriptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionSchedulerTest {

    @InjectMocks
    private SubscriptionScheduler subscriptionScheduler;

    @Mock
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("activateSubscriptions : 구독 활성화 서비스를 호출한다.")
    void activateSubscriptions() {
        willDoNothing().given(subscriptionService).activateSubscriptions();

        subscriptionScheduler.activateSubscriptions();

        then(subscriptionService).should().activateSubscriptions();
    }

    @Test
    @DisplayName("expireSubscriptions : 구독 만료 서비스를 호출한다.")
    void expireSubscriptions() {
        willDoNothing().given(subscriptionService).expireSubscriptions();

        subscriptionScheduler.expireSubscriptions();

        then(subscriptionService).should().expireSubscriptions();
    }
}

package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final StoreService storeService;
    private final StoreSubscriptionRepository storeSubscriptionRepository;

    public FindSubscriptionsResponse findSubscriptions(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);
        List<StoreSubscription> subscriptions = storeSubscriptionRepository.findAllByStore(store);
        return FindSubscriptionsResponse.from(subscriptions);
    }

    @Transactional
    public void confirmApplyPayment(Payment payment) {
        StoreSubscription storeSubscription = getByPayment(payment);

        storeSubscription.updateStartDate(LocalDate.now());
        storeSubscription.updateStatus(SubscriptionStatus.ACTIVE);
    }

    private StoreSubscription getByPayment(Payment payment) {
        return storeSubscriptionRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("결제에 대한 구독이 존재하지 않습니다."));
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }
}

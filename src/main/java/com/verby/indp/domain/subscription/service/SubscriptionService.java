package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private final StoreSubscriptionRepository storeSubscriptionRepository;
    private final StoreRepository storeRepository;

    public List<StoreSubscription> findSubscriptions(long storeId) {
        Store store = getStoreById(storeId);
        return storeSubscriptionRepository.findAllByStore(store);
    }

    private Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }
}

package com.verby.indp.domain.store.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;

    public FindStoresResponse findStores(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAllBySubscriptionStatus(SubscriptionStatus.ACTIVE, pageable);
        return FindStoresResponse.from(storePage.getContent());
    }

    public Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }
}

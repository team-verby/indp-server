package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.dto.response.FindOwnerPaymentsResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerPaymentService {

    private final StoreSubscriptionRepository storeSubscriptionRepository;
    private final StoreService storeService;

    public FindOwnerPaymentsResponse findPayments(Owner owner, long storeId, Pageable pageable) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);

        Page<StoreSubscription> page = storeSubscriptionRepository
            .findAllByStoreOrderByPaymentCreatedAtDesc(store, pageable);

        return FindOwnerPaymentsResponse.from(page);
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }
}

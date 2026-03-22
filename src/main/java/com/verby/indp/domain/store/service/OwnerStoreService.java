package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreApplyRepository;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerStoreService {

    private final StoreRepository storeRepository;
    private final StoreApplyRepository storeApplyRepository;
    private final OwnerRepository ownerRepository;

    public List<Store> findMyStores(long ownerId) {
        Owner owner = getOwner(ownerId);
        return storeRepository.findAllByOwner(owner);
    }

    public Store findMyStore(long ownerId, long storeId) {
        Owner owner = getOwner(ownerId);
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);
        return store;
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }

    private Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }

    private Owner getOwner(long ownerId) {
        return ownerRepository.findById(ownerId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));
    }
}

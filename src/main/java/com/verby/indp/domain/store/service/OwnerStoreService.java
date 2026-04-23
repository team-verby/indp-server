package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.dto.response.FindLatestSubscriptionResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByOwnerResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByOwnerResponse;
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

    public FindStoresByOwnerResponse getMyStores(Owner owner) {
        List<Store> stores = storeRepository.findAllByOwner(owner);
        return FindStoresByOwnerResponse.from(stores);
    }

    public FindStoreByOwnerResponse getMyStore(Owner owner, long storeId) {
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);
        return FindStoreByOwnerResponse.from(store);
    }

    @Transactional
    public void updateStore(Owner owner, long storeId, UpdateStoreRequest request) {
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);

        StoreMusic storeMusic = new StoreMusic(request.platform(), request.playedMusic(),
            request.rejectedSongNote(),
            request.playlistType(), request.musicTempo(), request.mood(), request.playMethods(),
            request.timePreferences(), request.preferenceGenres(), request.businessHours());

        store.update(request.name(), request.industry(), request.address(),
            request.customerAgeGroup(), request.lighting(), storeMusic, request.vibes(),
            request.businessHours(), request.photoUrls());
    }

    public FindLatestSubscriptionResponse getLatestSubscription(Owner owner, long storeId) {
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);
        return FindLatestSubscriptionResponse.from(store.getLatestSubscription());
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

}

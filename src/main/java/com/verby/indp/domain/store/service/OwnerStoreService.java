package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.*;
import com.verby.indp.domain.store.dto.request.UpdateStoreRequest;
import com.verby.indp.domain.store.dto.response.FindOwnerStoreResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerStoreService {

    private final StoreRepository storeRepository;

    public FindStoresResponse getMyStores(Owner owner) {
        List<Store> stores = storeRepository.findAllByOwner(owner);
        return FindStoresResponse.from(stores);
    }

    public FindOwnerStoreResponse getMyStore(Owner owner, long storeId) {
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);
        return FindOwnerStoreResponse.from(store);
    }

    @Transactional
    public void updateStore(Owner owner, long storeId, UpdateStoreRequest request) {
        Store store = getStoreById(storeId);
        validateOwnership(store, owner);

        List<StoreBusinessHour> businessHours = request.businessHours().stream()
            .map(bh -> new StoreBusinessHour(bh.dayOfWeek(), bh.openTime(), bh.closeTime(), bh.isClosed()))
            .toList();
        List<StorePhoto> photos = IntStream.range(0, request.photoUrls().size())
            .mapToObj(i -> new StorePhoto(request.photoUrls().get(i), i, i == 0))
            .toList();
        List<StoreVibe> vibes = request.vibes().stream()
            .map(StoreVibe::new)
            .toList();

        store.update(request.name(), request.industry(), request.address(),
            request.customerAgeGroup(), request.lighting(), businessHours, photos, vibes);

        List<PlayMethod> playMethods = request.playMethods().stream()
            .map(PlayMethod::new)
            .toList();
        List<MusicTimePreference> timePreferences = request.timePreferences().stream()
            .map(tp -> new MusicTimePreference(tp.startTime(), tp.endTime(), tp.mood()))
            .toList();
        List<MusicGenre> genres = request.preferenceGenres().stream()
            .map(g -> new MusicGenre(g.genre(), g.preferenceType()))
            .toList();

        store.getStoreMusic().update(request.platform(), request.playedMusic(), request.rejectedSongNote(),
            request.playlistType(), request.musicTempo(), request.mood(), playMethods, timePreferences, genres);
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

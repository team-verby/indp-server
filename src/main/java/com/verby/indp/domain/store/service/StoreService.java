package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.store.*;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.store.repository.StoreApplyRepository;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final StoreApplyRepository storeApplyRepository;

    @Transactional
    public ApplyStoreResponse applyStore(ApplyStoreRequest request) {
        String loginId = generateUniqueLoginId();
        String password = generatePassword();

        Owner owner = ownerRepository.save(
            new Owner(loginId, password, request.applicantName(), request.applicantPhone()));

        StoreApply storeApply = storeApplyRepository.save(
            new StoreApply(request.applicantName(), request.applicantPhone(), request.inquiryContent()));

        Store store = storeRepository.save(new Store(
            storeApply, owner, request.name(), request.industry(), request.address(),
            request.customerAgeGroup(), request.lighting()));

        for (ApplyStoreRequest.BusinessHour bh : request.businessHours()) {
            store.getBusinessHours().add(
                new StoreBusinessHour(store, bh.dayOfWeek(), bh.openTime(), bh.closeTime(), bh.isClosed()));
        }

        for (int i = 0; i < request.photoUrls().size(); i++) {
            store.getPhotos().add(new StorePhoto(store, request.photoUrls().get(i), i, i == 0));
        }

        for (StoreMood.Vibe vibe : request.moods()) {
            store.getMoods().add(new StoreMood(store, vibe));
        }

        for (PlayMethod.Method method : request.playMethods()) {
            store.getPlayMethods().add(new PlayMethod(store, method));
        }

        for (String genre : request.rejectedGenres()) {
            store.getGenres().add(new StoreGenre(store, genre));
        }

        if (request.timePreferences() != null) {
            for (ApplyStoreRequest.TimePreference tp : request.timePreferences()) {
                store.getMusicTimePreferences().add(
                    new StoreMusicTimePreference(store, tp.startTime(), tp.endTime(), tp.mood()));
            }
        }

        StoreMusic storeMusic = new StoreMusic(
            store, request.platform(), request.playedMusic(),
            request.playlistType(), request.vibe(), request.tempo(), request.rejectedSongNote());
        store.setStoreMusic(storeMusic);

        return new ApplyStoreResponse(loginId, password);
    }


    public Page<Store> findStores(Pageable pageable) {
        return storeRepository.findAllByOrderByStoreIdAsc(pageable);
    }

    public Store findStore(long storeId) {
        return getStoreById(storeId);
    }

    private Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }

    private String generateUniqueLoginId() {
        String loginId;
        do {
            loginId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        } while (ownerRepository.existsByLoginId(loginId));
        return loginId;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}

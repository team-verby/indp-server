package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.service.OwnerService;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreApply;
import com.verby.indp.domain.store.StoreMusic;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.AddFirstSubscriptionResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.dto.request.AddSubscriptionRequest;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.notification.ApplicationMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplyStoreService {

    private final StoreRepository storeRepository;
    private final OwnerService ownerService;
    private final SubscriptionService subscriptionService;
    private final ApplicationMailService applicationMailService;

    @Transactional
    public AddFirstSubscriptionResponse applyStore(ApplyStoreRequest request) {
        validateDuplicateName(request.name());
        Store store = buildStore(request);
        storeRepository.save(store);

        AddSubscriptionRequest addSubscriptionRequest = new AddSubscriptionRequest(request.planId(),
            request.usagePeriod());
        AddFirstSubscriptionResponse response =
            subscriptionService.orderFirstSubscription(store, addSubscriptionRequest);

        applicationMailService.notifyStoreApplication(
            request.name(), request.applicantName(), request.applicantPhone(),
            request.planId(), request.usagePeriod(),
            store.getOwner().getLoginId(), store.getOwner().getPassword());

        return response;
    }

    private void validateDuplicateName(String name) {
        if (storeRepository.existsByName(name)) {
            throw new BadRequestException("이미 존재하는 매장 이름입니다.");
        }
    }

    private Store buildStore(ApplyStoreRequest request) {
        Owner owner = ownerService.createOwner(request.applicantName(), request.name());

        StoreApply storeApply = new StoreApply(request.applicantName(), request.applicantPhone());
        StoreMusic storeMusic = new StoreMusic(request.platform(), request.playedMusic(),
            request.rejectedSongNote(),
            request.playlistType(), request.musicTempo(), request.mood(), request.playMethods(),
            request.timePreferences(), request.preferenceGenres(), request.businessHours());

        return new Store(storeApply, owner, request.name(), request.industry(), request.address(),
            request.customerAgeGroup(), request.lighting(), storeMusic, request.vibes(),
            request.businessHours(), request.photoUrls());
    }

}

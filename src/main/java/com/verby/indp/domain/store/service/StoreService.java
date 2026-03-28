package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.store.*;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.StoreSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private static final String ORDER_NAME_PREFIX = "인디피_구독_";
    private static final String OWNER_LOGIN_ID_PREFIX = "store";

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final PlanService planService;

    @Transactional
    public ApplyStoreResponse applyStore(ApplyStoreRequest request) {
        Store store = buildStore(request);

        Plan plan = planService.getPlan(request.planId());
        Payment payment = buildPayment(request.name(), plan, request.usagePeriod());

        StoreSubscription storeSubscription = new StoreSubscription(plan, payment, request.usagePeriod());
        store.addSubscription(storeSubscription);

        return ApplyStoreResponse.from(storeSubscription);
    }

    public Page<Store> findStores(Pageable pageable) {
        return storeRepository.findAllByOrderByStoreIdAsc(pageable);
    }

    public Store findStore(long storeId) {
        return getStoreById(storeId);
    }

    public Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }

    private Payment buildPayment(String storeName, Plan plan, int usagePeriod) {
        int amount = calculateAmount(plan, usagePeriod);
        String orderName = createOrderName(storeName);
        return new Payment(orderName, amount);
    }

    private String createOrderName(String storeName) {
        return ORDER_NAME_PREFIX + storeName;
    }

    private Store buildStore(ApplyStoreRequest request) {
        StoreMusic storeMusic = buildStoreMusic(request);
        String loginId = generateUniqueLoginId();
        String password = generatePassword();
        Owner owner = new Owner(loginId, password, request.applicantName(), request.applicantPhone());
        StoreApply storeApply = new StoreApply(request.applicantName(), request.applicantPhone(), request.inquiryContent());
        List<StoreBusinessHour> storeBusinessHours = request.businessHours().stream()
            .map(bh -> new StoreBusinessHour(bh.dayOfWeek(), bh.openTime(), bh.closeTime(), bh.isClosed()))
            .toList();
        List<StorePhoto> storePhotos = IntStream.range(0, request.photoUrls().size())
            .mapToObj(i -> new StorePhoto(request.photoUrls().get(i), i, i == 0))
            .toList();
        List<StoreVibe> storeVibes = request.vibes().stream()
            .map(StoreVibe::new)
            .toList();

        return new Store(storeApply, owner, request.name(), request.industry(), request.address(), request.customerAgeGroup(), request.lighting(), storeMusic, storeVibes, storeBusinessHours, storePhotos);
    }

    private StoreMusic buildStoreMusic(ApplyStoreRequest request) {
        List<PlayMethod> playMethods = request.playMethods().stream()
            .map(PlayMethod::new)
            .toList();
        List<MusicGenre> musicGenres = request.preferenceGenres().stream()
            .map(preferenceGenre -> new MusicGenre(preferenceGenre.genre(), preferenceGenre.preferenceType()))
            .toList();
        List<MusicTimePreference> musicTimePreferences = request.timePreferences().stream()
            .map(timePreference -> new MusicTimePreference(timePreference.startTime(), timePreference.endTime(), timePreference.mood()))
            .toList();

        return new StoreMusic(request.platform(), request.playedMusic(), request.rejectedSongNote(),
            request.playlistType(), request.musicTempo(), request.mood(), playMethods, musicTimePreferences, musicGenres);
    }

    private int calculateAmount(Plan plan, int usagePeriod) {
        int monthlyPrice = plan.getDiscounts().stream()
                .filter(PlanDiscount::isActive)
                .findFirst()
                .map(d -> plan.getMonthlyPrice() * (100 - d.getDiscountRate()) / 100)
                .orElse(plan.getMonthlyPrice());

        return monthlyPrice * usagePeriod;
    }

    private String generateUniqueLoginId() {
        String loginId;
        Random random = new Random();
        do {
            loginId = OWNER_LOGIN_ID_PREFIX + String.format("%04d", random.nextInt(10000));
        } while (ownerRepository.existsByLoginId(loginId));

        return loginId;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}

package com.verby.indp.domain.store.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;
import com.verby.indp.domain.plan.repository.PlanRepository;
import com.verby.indp.domain.store.*;
import com.verby.indp.domain.store.dto.request.ApplyStoreRequest;
import com.verby.indp.domain.store.dto.response.ApplyStoreResponse;
import com.verby.indp.domain.store.repository.StoreApplyRepository;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final StoreApplyRepository storeApplyRepository;
    private final PlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final StoreSubscriptionRepository storeSubscriptionRepository;

    @Transactional
    public ApplyStoreResponse applyStore(ApplyStoreRequest request) {
        List<PlayMethod> playMethods = request.playMethods().stream()
                .map(PlayMethod::new)
                .toList();
        List<MusicGenre> musicGenres = request.preferenceGenres().stream()
                .map(preferenceGenre -> new MusicGenre(preferenceGenre.genre(), preferenceGenre.preferenceType()))
                .toList();
        List<MusicTimePreference> musicTimePreferences = request.timePreferences().stream()
                .map(timePreference -> new MusicTimePreference(timePreference.startTime(), timePreference.endTime(), timePreference.mood()))
                .toList();
        StoreMusic storeMusic = new StoreMusic(request.platform(), request.playedMusic(), request.rejectedSongNote(),
                request.playlistType(), request.musicTempo(), request.mood(), playMethods, musicTimePreferences, musicGenres);

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

        Store store = new Store(storeApply, owner, request.name(), request.industry(), request.address(), request.customerAgeGroup(), request.lighting(), storeMusic, storeVibes, storeBusinessHours, storePhotos);
        storeRepository.save(store);

        Plan plan = planRepository.findById(request.planId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 플랜입니다."));

        int amount = calculateAmount(plan, request.usagePeriod());

        String orderName = "인디피_구독_" + request.name();
        Payment payment = paymentRepository.save(new Payment(orderName, amount));

        StoreSubscription storeSubscription = new StoreSubscription(store, plan, payment, request.usagePeriod());
        storeSubscriptionRepository.save(storeSubscription);

        return new ApplyStoreResponse(payment.getOrderId(), payment.getAmount(), payment.getOrderName());
    }

    @Transactional
    public void confirmApplyPayment(Payment payment) {
        StoreSubscription storeSubscription = storeSubscriptionRepository.findByPayment(payment)
                .orElseThrow(() -> new NotFoundException("구독 정보가 존재하지 않습니다."));
        storeSubscription.updateStartDate(LocalDate.now());
        storeSubscription.updateStatus(SubscriptionStatus.ACTIVE);
    }

    public Page<Store> findStores(Pageable pageable) {
        return storeRepository.findAllByOrderByStoreIdAsc(pageable);
    }

    public Store findStore(long storeId) {
        return getStoreById(storeId);
    }

    private int calculateAmount(Plan plan, int usagePeriod) {
        int monthlyPrice = plan.getDiscounts().stream()
                .filter(PlanDiscount::isActive)
                .findFirst()
                .map(d -> plan.getMonthlyPrice() * (100 - d.getDiscountRate()) / 100)
                .orElse(plan.getMonthlyPrice());
        return monthlyPrice * usagePeriod;
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

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
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

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
        // TODO: 매장 도입, 점주 등록, 구독 등록, 결제 라이프 사이클
        String loginId = generateUniqueLoginId();

        Owner owner = ownerRepository.save(
            new Owner(loginId, UUID.randomUUID().toString(), request.applicantName(), request.applicantPhone()));

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

        Plan plan = planRepository.findById(request.planId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 플랜입니다."));

        int monthlyPrice = plan.getDiscounts().stream()
            .filter(PlanDiscount::isActive)
            .findFirst()
            .map(d -> plan.getMonthlyPrice() * (100 - d.getDiscountRate()) / 100)
            .orElse(plan.getMonthlyPrice());
        int amount = monthlyPrice * request.usagePeriod();

        String orderName = "인디피_구독_" + store.getName();
        Payment payment = paymentRepository.save(new Payment(orderName, amount));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(request.usagePeriod());
        storeSubscriptionRepository.save(new StoreSubscription(store, plan, payment, startDate, endDate));

        return new ApplyStoreResponse(payment.getOrderId(), payment.getAmount(), payment.getOrderName());
    }

    @Transactional
    public void confirmApplyPayment(Payment payment) {
        Store store = storeRepository.findById(payment.getStoreId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
        Plan plan = planRepository.findById(payment.getPlanId())
            .orElseThrow(() -> new NotFoundException("존재하지 않는 플랜입니다."));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(payment.getUsagePeriod());
        storeSubscriptionRepository.save(new StoreSubscription(store, plan, payment, startDate, endDate));

        String newPassword = generatePassword();
        store.getOwner().updatePassword(newPassword);
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

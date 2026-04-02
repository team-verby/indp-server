package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.subscription.dto.request.RenewSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private static final String ORDER_NAME_PREFIX = "인디피_구독갱신_";

    private final StoreService storeService;
    private final PlanService planService;
    private final StoreSubscriptionRepository storeSubscriptionRepository;

    @Transactional
    public StoreSubscription renewSubscription(Owner owner, long storeId, RenewSubscriptionRequest request) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);

        Plan plan = planService.getPlan(request.planId());
        int amount = calculateAmount(plan, request.usagePeriod());
        Payment payment = new Payment(ORDER_NAME_PREFIX + store.getName(), amount);

        StoreSubscription subscription = new StoreSubscription(plan, payment, request.usagePeriod());
        store.addSubscription(subscription);

        return subscription;
    }

    public FindSubscriptionsResponse findSubscriptions(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);
        List<StoreSubscription> subscriptions = storeSubscriptionRepository.findAllByStore(store);
        return FindSubscriptionsResponse.from(subscriptions);
    }

    @Transactional
    public void expireSubscriptions() {
        List<StoreSubscription> expired = storeSubscriptionRepository
            .findAllByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, LocalDate.now());
        expired.forEach(s -> s.updateStatus(SubscriptionStatus.EXPIRED));
    }

    @Transactional
    public void confirmPayment(Payment payment) {
        StoreSubscription subscription = getByPayment(payment);
        LocalDate endDate = subscription.getStore()
                .getLatestSubscription().getEndDate();
        LocalDate startDate = endDate.isBefore(LocalDate.now()) ? LocalDate.now() : endDate.plusDays(1);

        subscription.updateStartDate(startDate);
        subscription.updateStatus(SubscriptionStatus.ACTIVE);
    }

    private int calculateAmount(Plan plan, int usagePeriod) {
        int monthlyPrice = plan.getDiscounts().stream()
            .filter(PlanDiscount::isActive)
            .findFirst()
            .map(d -> plan.getMonthlyPrice() * (100 - d.getDiscountRate()) / 100)
            .orElse(plan.getMonthlyPrice());
        return monthlyPrice * usagePeriod;
    }

    private StoreSubscription getByPayment(Payment payment) {
        return storeSubscriptionRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("결제에 대한 구독이 존재하지 않습니다."));
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }
}

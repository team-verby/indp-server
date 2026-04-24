package com.verby.indp.domain.subscription.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.plan.PlanDiscount;
import com.verby.indp.domain.plan.service.PlanService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.AddSubscriptionResponse;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.subscription.dto.request.AddSubscriptionRequest;
import com.verby.indp.domain.subscription.dto.response.FindSubscriptionsResponse;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import com.verby.indp.global.slack.SlackNotificationService;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

    private static final String ORDER_NAME_PREFIX = "인디피_구독_";
    private static final DayOfWeek SUBSCRIPTION_START_DAY = DayOfWeek.TUESDAY;

    private final StoreService storeService;
    private final PlanService planService;
    private final StoreSubscriptionRepository storeSubscriptionRepository;
    private final SlackNotificationService slackNotificationService;
    private final Clock clock;

    @Transactional
    public AddSubscriptionResponse orderSubscription(Owner owner, long storeId,
        AddSubscriptionRequest request) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);

        Plan plan = planService.getPlan(request.planId());
        Payment payment = buildPayment(store.getName(), plan, request.usagePeriod());

        LocalDate startDate = resolveStartDate(store);
        StoreSubscription subscription = new StoreSubscription(plan, payment, request.usagePeriod(),
            startDate);
        store.addSubscription(subscription);

        return AddSubscriptionResponse.from(subscription);
    }

    public FindSubscriptionsResponse findSubscriptions(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);
        validateOwnership(store, owner);
        List<StoreSubscription> subscriptions = storeSubscriptionRepository.findAllByStore(store);
        return FindSubscriptionsResponse.from(subscriptions);
    }

    @Transactional
    public void activateSubscriptions() {
        List<StoreSubscription> toActivate = storeSubscriptionRepository
            .findAllByStatusAndStartDateLessThanEqual(SubscriptionStatus.PENDING_ACTIVE,
                LocalDate.now(clock));
        toActivate.forEach(s -> s.updateStatus(SubscriptionStatus.ACTIVE));
    }

    @Transactional
    public void expireSubscriptions() {
        List<StoreSubscription> expired = storeSubscriptionRepository
            .findAllByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, LocalDate.now(clock));
        expired.forEach(s -> s.updateStatus(SubscriptionStatus.EXPIRED));
    }

    @Transactional
    public void confirmPayment(Payment payment) {
        StoreSubscription subscription = getByPayment(payment);
        subscription.updateStatus(SubscriptionStatus.PENDING_ACTIVE);
        slackNotificationService.handleApplyStoreStore(subscription.getStore());
    }

    private Payment buildPayment(String storeName, Plan plan, int usagePeriod) {
        int amount = calculateAmount(plan, usagePeriod);
        String orderName = createOrderName(storeName);
        return new Payment(orderName, amount);
    }

    private String createOrderName(String storeName) {
        return ORDER_NAME_PREFIX + storeName;
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

    private LocalDate resolveStartDate(Store store) {
        return store.findLatestPaidSubscription()
            .map(subscription -> subscription.getEndDate().plusDays(1))
            .orElseGet(this::nextSubscriptionStartDay);
    }

    private LocalDate nextSubscriptionStartDay() {
        LocalDate thisMonday = LocalDate.now(clock)
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int daysUntilStartDay = SUBSCRIPTION_START_DAY.getValue() - DayOfWeek.MONDAY.getValue();
        return thisMonday.plusWeeks(1).plusDays(daysUntilStartDay);
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }
}

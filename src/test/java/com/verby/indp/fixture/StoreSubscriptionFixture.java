package com.verby.indp.fixture;

import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import java.time.LocalDate;

public class StoreSubscriptionFixture {

    public static StoreSubscription activeSubscription() {
        StoreSubscription subscription = new StoreSubscription(
            PlanFixture.planA(),
            PaymentFixture.payment(),
            12,
            LocalDate.of(2026, 1, 12)
        );
        subscription.updateStatus(SubscriptionStatus.ACTIVE);
        return subscription;
    }

    public static StoreSubscription activeSubscriptionWithPlan(Plan plan) {
        StoreSubscription subscription = new StoreSubscription(
            plan,
            PaymentFixture.payment(),
            12,
            LocalDate.of(2026, 1, 12)
        );
        subscription.updateStatus(SubscriptionStatus.ACTIVE);
        return subscription;
    }

    public static StoreSubscription pendingPaymentSubscription() {
        return new StoreSubscription(
            PlanFixture.planA(),
            PaymentFixture.payment(),
            12,
            LocalDate.of(2026, 1, 12)
        );
    }

    public static StoreSubscription pendingActiveSubscription() {
        StoreSubscription subscription = new StoreSubscription(
            PlanFixture.planA(),
            PaymentFixture.payment(),
            12,
            LocalDate.of(2026, 1, 12)
        );
        subscription.updateStatus(SubscriptionStatus.PENDING_ACTIVE);
        return subscription;
    }
}

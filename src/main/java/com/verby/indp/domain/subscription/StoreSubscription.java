package com.verby.indp.domain.subscription;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "store_subscription")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class StoreSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_subscription_id")
    private Long storeSubscriptionId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "usage_period")
    private int usagePeriod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionStatus status = SubscriptionStatus.PENDING_PAYMENT;

    public StoreSubscription(Plan plan, Payment payment, int usagePeriod, LocalDate startDate) {
        validatePlan(plan);
        validatePayment(payment);
        validateUsagePeriod(usagePeriod);
        validateStartDate(startDate);
        this.plan = plan;
        this.payment = payment;
        this.usagePeriod = usagePeriod;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(usagePeriod);
    }


    public void updateStatus(SubscriptionStatus status) {
        this.status = status;
    }

    private void validatePlan(Plan plan) {
        if (plan == null) {
            throw new BadRequestException("plan은 필수입니다.");
        }
    }

    private void validatePayment(Payment payment) {
        if (payment == null) {
            throw new BadRequestException("payment는 필수입니다.");
        }
    }

    private void validateUsagePeriod(int usagePeriod) {
        if (usagePeriod <= 0) {
            throw new BadRequestException("usagePeriod는 양수여야 합니다.");
        }
    }

    private void validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new BadRequestException("startDate는 필수입니다.");
        }
    }
}

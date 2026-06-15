package com.verby.indp.domain.subscription;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.payment.Payment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_subscription")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class UserSubscription extends BaseTimeEntity {

    private static final String PLAN_NAME = "Plan A 라이트 요금제";
    private static final int MONTHLY_RATE = 4400;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_subscription_id")
    private Long userSubscriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "usage_period", nullable = false)
    private int usagePeriod;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserSubscriptionStatus status = UserSubscriptionStatus.PENDING_PAYMENT;

    public UserSubscription(User user, Payment payment, int usagePeriod) {
        this.user = user;
        this.payment = payment;
        this.usagePeriod = usagePeriod;
    }

    public void activate(LocalDate today) {
        this.status = UserSubscriptionStatus.ACTIVE;
        this.startDate = today;
        this.endDate = today.plusMonths(usagePeriod).minusDays(1);
    }

    public String getPlanName() {
        return PLAN_NAME;
    }

    public int getMonthlyRate() {
        return MONTHLY_RATE;
    }
}

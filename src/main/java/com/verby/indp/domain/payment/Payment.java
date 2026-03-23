package com.verby.indp.domain.payment;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.verby.indp.domain.payment.PaymentStatus.ABORTED;
import static com.verby.indp.domain.payment.PaymentStatus.DONE;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "amount")
    private int amount;

    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "usage_period")
    private Integer usagePeriod;

    public Payment(String orderName, int amount) {
        this.orderId = UUID.randomUUID().toString();
        this.orderName = orderName;
        this.amount = amount;
    }

    public Payment(String orderName, int amount, Long storeId, Long planId, int usagePeriod) {
        this.orderId = UUID.randomUUID().toString();
        this.orderName = orderName;
        this.amount = amount;
        this.storeId = storeId;
        this.planId = planId;
        this.usagePeriod = usagePeriod;
    }

    public void updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
    }

    public void success() {
        this.status = DONE;
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = ABORTED;
    }

    public boolean isFail() {
        return this.status == ABORTED;
    }

    public boolean isSuccess() {
        return this.status == DONE;
    }

    public boolean isDifferentAmount(int amount) {
        return this.amount != amount;
    }
}

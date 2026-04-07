package com.verby.indp.domain.payment;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.verby.indp.domain.payment.PaymentStatus.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.PENDING;

    public Payment(String orderName, int amount) {
        validateOrderName(orderName);
        validateAmount(amount);
        this.orderId = UUID.randomUUID().toString();
        this.orderName = orderName;
        this.amount = amount;
    }

    public void updatePaymentKey(String paymentKey) {
        validatePaymentKey(paymentKey);
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

    private void validateOrderName(String orderName) {
        if (orderName == null || orderName.isBlank()) {
            throw new BadRequestException("orderName은 필수입니다.");
        }
    }

    private void validateAmount(int amount) {
        if (amount <= 0) {
            throw new BadRequestException("amount는 양수여야 합니다.");
        }
    }

    private void validatePaymentKey(String paymentKey) {
        if (paymentKey == null || paymentKey.isBlank()) {
            throw new BadRequestException("paymentKey는 필수입니다.");
        }
    }
}

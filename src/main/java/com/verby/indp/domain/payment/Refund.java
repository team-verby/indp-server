package com.verby.indp.domain.payment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "refund")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long refundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(name = "cancel_amount")
    private int cancelAmount;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "refunded_at")
    private LocalDateTime refundedAt;

    public Refund(Payment payment, int cancelAmount, String cancelReason, LocalDateTime refundedAt) {
        this.payment = payment;
        this.cancelAmount = cancelAmount;
        this.cancelReason = cancelReason;
        this.refundedAt = refundedAt;
    }
}

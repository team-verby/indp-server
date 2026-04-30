package com.verby.indp.fixture;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import java.time.LocalDateTime;


public class PaymentFixture {

    public static Payment payment() {
        return new Payment(PaymentType.SUBSCRIPTION, "인디피_구독_카페공명", 180000);
    }

    public static Payment payment(PaymentType type, String orderName, int amount) {
        return new Payment(type, orderName, amount);
    }

    public static Payment donePayment() {
        Payment payment = new Payment(PaymentType.SUBSCRIPTION, "인디피_구독_카페공명", 180000);
        payment.success(LocalDateTime.now());
        return payment;
    }
}

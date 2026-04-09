package com.verby.indp.fixture;

import com.verby.indp.domain.payment.Payment;

public class PaymentFixture {

    public static Payment payment() {
        return new Payment("인디피_구독_카페공명", 180000);
    }

    public static Payment payment(String orderName, int amount) {
        return new Payment(orderName, amount);
    }
}

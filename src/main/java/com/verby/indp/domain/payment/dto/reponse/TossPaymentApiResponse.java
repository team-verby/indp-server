package com.verby.indp.domain.payment.dto.reponse;

public record TossPaymentApiResponse(String method, String status, int balanceAmount, int totalAmount) {

}

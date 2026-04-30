package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.payment.dto.reponse.TossErrorResponse;
import com.verby.indp.domain.payment.dto.reponse.TossPaymentApiResponse;
import com.verby.indp.domain.payment.exception.TossPaymentFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentClient {

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    private static final String confirmUrl = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String cancelUrl = "https://api.tosspayments.com/v1/payments/%s/cancel";

    private final WebClient webClient;

    public void confirmPayment(String orderId, String paymentKey, int amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);

        TossPaymentApiResponse response = post(confirmUrl, params);
        log.info("결제 승인 완료. paymentKey={}, status={}, totalAmount={}, balanceAmount={}", paymentKey, response.status(), response.totalAmount(), response.balanceAmount());
    }

    public TossPaymentApiResponse cancelPayment(String paymentKey, int cancelAmount, String cancelReason) {
        Map<String, Object> params = new HashMap<>();
        params.put("cancelReason", cancelReason);
        params.put("cancelAmount", cancelAmount);

        TossPaymentApiResponse response = post(String.format(cancelUrl, paymentKey), params);
        log.info("결제 취소 완료. paymentKey={}, status={}, totalAmount={}, balanceAmount={}", paymentKey, response.status(), response.totalAmount(), response.balanceAmount());
        return response;
    }

    private <T> T post(String url, Map<String, Object> body) {
        return webClient.post()
            .uri(url)
            .headers(this::setHeaders)
            .bodyValue(body)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response ->
                response.bodyToMono(TossErrorResponse.class)
                    .map(err -> new TossPaymentFailException(err.message()))
            )
            .bodyToMono((Class<T>) TossPaymentApiResponse.class)
            .block();
    }

    private void setHeaders(HttpHeaders headers) {
        headers.setBasicAuth(getEncodeAuth());
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private String getEncodeAuth() {
        return new String(
            Base64.getEncoder()
                .encode((secretKey + ":").getBytes(StandardCharsets.UTF_8))
        );
    }
}

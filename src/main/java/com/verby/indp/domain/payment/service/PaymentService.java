package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final StoreService storeService;
    private final SongRecommendationService songRecommendationService;

    @Transactional
    public void confirm(ConfirmPaymentRequest request) {
        Payment payment = getPayment(request.orderId());

        validatePaymentStatus(payment);
        validatePaymentAmount(payment, request.amount());

        paymentClient.confirmPayment(request.orderId(), request.paymentKey(), request.amount());

        payment.updatePaymentKey(request.paymentKey());
        payment.success();

        if (request.paymentType() == PaymentType.STORE_APPLY) {
            storeService.confirmApplyPayment(payment);
        } else if (request.paymentType() == PaymentType.SONG_RECOMMENDATION) {
            songRecommendationService.confirmPayment(payment);
        }
    }

    @Transactional
    public void failPayment(String orderId) {
        Payment payment = getPayment(orderId);
        validatePaymentStatus(payment);
        payment.fail();
    }

    private void validatePaymentStatus(Payment payment) {
        if (payment.isFail() || payment.isSuccess()) {
            throw new PaymentBadRequestException(
                MessageFormat.format("이미 처리된 결제입니다. 결제 상태: {0}", payment.getStatus()));
        }
    }

    private void validatePaymentAmount(Payment payment, int amount) {
        if (payment.isDifferentAmount(amount)) {
            throw new PaymentBadRequestException("결제 금액이 일치하지 않습니다");
        }
    }

    private Payment getPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다."));
    }
}

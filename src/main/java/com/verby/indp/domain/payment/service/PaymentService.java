package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void failPayment(String orderId) {
        Payment payment = getPaymentByOrderId(orderId);
        if (!payment.isStatusWith(PaymentStatus.PENDING)) {
            throw new PaymentBadRequestException("이미 처리된 결제입니다.");
        }
        payment.fail();
    }

    public Payment getPaymentById(long id) {
        return paymentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다."));
    }

    private Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new NotFoundException("결제 정보가 존재하지 않습니다."));
    }
}

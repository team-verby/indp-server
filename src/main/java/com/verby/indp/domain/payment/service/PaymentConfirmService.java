package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.request.ConfirmPaymentRequest;
import com.verby.indp.domain.payment.exception.PaymentBadRequestException;
import com.verby.indp.domain.payment.exception.TossPaymentFailException;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentConfirmService {

    private final Map<PaymentType, PaymentConfirmHandler> handlers;
    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final PaymentService paymentService;
    private final Clock clock;

    public PaymentConfirmService(
        PaymentRepository paymentRepository,
        PaymentClient paymentClient,
        List<PaymentConfirmHandler> handlers,
        PaymentService paymentService,
        Clock clock
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentClient = paymentClient;
        this.handlers = handlers.stream()
            .collect(Collectors.toMap(PaymentConfirmHandler::supportedType, Function.identity()));
        this.paymentService = paymentService;
        this.clock = clock;
    }

    @Transactional
    public void confirm(ConfirmPaymentRequest request) {
        Payment payment = getPayment(request.orderId());

        validatePaymentStatus(payment);
        validatePaymentAmount(payment, request.amount());

        getHandler(request.paymentType()).handle(payment);

        try {
            paymentClient.confirmPayment(request.orderId(), request.paymentKey(), request.amount());
        } catch (TossPaymentFailException e) {
            paymentService.failPayment(request.orderId());
            throw e;
        }

        payment.updatePaymentKey(request.paymentKey());
        payment.success(LocalDateTime.now(clock));
    }

    private PaymentConfirmHandler getHandler(PaymentType paymentType) {
        PaymentConfirmHandler handler = handlers.get(paymentType);
        if (handler == null) {
            throw new PaymentBadRequestException(
                MessageFormat.format("지원하지 않는 결제 타입입니다: {0}", paymentType));
        }
        return handler;
    }

    private void validatePaymentStatus(Payment payment) {
        if (!payment.isStatusWith(PaymentStatus.PENDING)) {
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

package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.payment.dto.reponse.TossPaymentApiResponse;
import com.verby.indp.domain.payment.dto.request.RefundPaymentRequest;
import com.verby.indp.domain.payment.dto.response.FindAdminPaymentsResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPaymentService {

    private final StoreSubscriptionRepository storeSubscriptionRepository;
    private final StoreService storeService;
    private final PaymentClient paymentClient;
    private final PaymentService paymentService;
    private final SubscriptionService subscriptionService;

    public FindAdminPaymentsResponse findPayments(long storeId, Pageable pageable) {
        Store store = storeService.getStoreById(storeId);

        Page<StoreSubscription> page = storeSubscriptionRepository
            .findAllByStoreOrderByPaymentCreatedAtDesc(store, pageable);

        return FindAdminPaymentsResponse.from(page);
    }

    @Transactional
    public void refundPayment(long paymentId, RefundPaymentRequest request) {
        Payment payment = paymentService.getPaymentById(paymentId);
        validateCancelAmount(request.cancelAmount(), payment.getBalanceAmount());

        TossPaymentApiResponse response = paymentClient.cancelPayment(
            payment.getPaymentKey(), request.cancelAmount(), request.cancelReason());

        PaymentStatus status = response.balanceAmount() == 0
            ? PaymentStatus.CANCELED : PaymentStatus.PARTIAL_CANCELED;
        payment.refund(status, response.balanceAmount(), request.cancelAmount(), request.cancelReason());

        cancelSubscriptionIfNeeded(payment);
    }

    private void cancelSubscriptionIfNeeded(Payment payment) {
        if (payment.getType() == PaymentType.SUBSCRIPTION) {
            subscriptionService.cancelSubscriptionByPayment(payment);
        }
    }

    private void validateCancelAmount(int cancelAmount, int balanceAmount) {
        if (cancelAmount > balanceAmount) {
            throw new BadRequestException("환불 요청 금액이 잔액을 초과합니다.");
        }
    }
}

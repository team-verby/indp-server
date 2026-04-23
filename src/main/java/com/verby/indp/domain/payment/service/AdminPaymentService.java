package com.verby.indp.domain.payment.service;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentStatus;
import com.verby.indp.domain.payment.dto.reponse.TossPaymentApiResponse;
import com.verby.indp.domain.payment.dto.request.CancelPaymentRequest;
import com.verby.indp.domain.payment.dto.response.FindAdminPaymentsResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.repository.StoreSubscriptionRepository;
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

    public FindAdminPaymentsResponse findPayments(long storeId, Pageable pageable) {
        Store store = storeService.getStoreById(storeId);

        Page<StoreSubscription> page = storeSubscriptionRepository
            .findAllByStoreOrderByPaymentCreatedAtDesc(store, pageable);

        return FindAdminPaymentsResponse.from(page);
    }

    public void cancelPayment(long paymentId, CancelPaymentRequest request) {
        Payment payment = paymentService.getPaymentById(paymentId);
        TossPaymentApiResponse response = paymentClient.cancelPayment(payment.getPaymentKey(), request.cancelAmount(), request.cancelReason());
        int balanceAmount = response.balanceAmount();

        if (balanceAmount == 0) {
            payment.cancel(PaymentStatus.CANCELED, balanceAmount);
        } else {
            payment.cancel(PaymentStatus.PARTIAL_CANCELED, balanceAmount);
        }
    }
}

package com.verby.indp.domain.payment.dto.response;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.subscription.StoreSubscription;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindAdminPaymentsResponse(
    List<PaymentItem> payments,
    int totalPages,
    long totalElements
) {

    public static FindAdminPaymentsResponse from(Page<StoreSubscription> page) {
        List<PaymentItem> items = page.getContent().stream()
            .map(PaymentItem::from)
            .toList();
        return new FindAdminPaymentsResponse(items, page.getTotalPages(), page.getTotalElements());
    }

    private record PaymentItem(
        long paymentId,
        LocalDateTime paidAt,
        String paymentStatus,
        int totalAmount,
        int balanceAmount,
        SubscriptionItem subscription
    ) {

        private static PaymentItem from(StoreSubscription subscription) {
            Payment payment = subscription.getPayment();
            return new PaymentItem(
                payment.getPaymentId(),
                payment.getPaidAt(),
                payment.getStatus().name(),
                payment.getTotalAmount(),
                payment.getBalanceAmount(),
                SubscriptionItem.from(subscription)
            );
        }

        private record SubscriptionItem(
            String plan,
            LocalDate startDate,
            LocalDate endDate
        ) {
            private static SubscriptionItem from(StoreSubscription subscription) {
                return new SubscriptionItem(
                    subscription.getPlan().getType().name(),
                    subscription.getStartDate(),
                    subscription.getEndDate()
                );
            }

        }
    }
}

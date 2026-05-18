package com.verby.indp.domain.payment.dto.response;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.Refund;
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
        List<RefundItem> refunds,
        SubscriptionItem subscription
    ) {

        private static PaymentItem from(StoreSubscription subscription) {
            Payment payment = subscription.getPayment();
            List<RefundItem> refundItems = payment.getRefunds().stream()
                .map(RefundItem::from)
                .toList();
            return new PaymentItem(
                payment.getPaymentId(),
                payment.getPaidAt(),
                payment.getStatus().name(),
                payment.getTotalAmount(),
                payment.getBalanceAmount(),
                refundItems,
                SubscriptionItem.from(subscription)
            );
        }

        private record RefundItem(
            long refundId,
            int cancelAmount,
            String cancelReason,
            LocalDateTime refundedAt
        ) {
            private static RefundItem from(Refund refund) {
                return new RefundItem(
                    refund.getRefundId(),
                    refund.getCancelAmount(),
                    refund.getCancelReason(),
                    refund.getRefundedAt()
                );
            }
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

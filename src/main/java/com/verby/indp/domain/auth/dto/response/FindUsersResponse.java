package com.verby.indp.domain.auth.dto.response;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.subscription.UserSubscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FindUsersResponse(
    List<UserItem> users
) {

    public record UserItem(
        Long userId,
        String loginId,
        String name,
        String email,
        String subscriptionStatus,
        LocalDateTime paidAt,
        LocalDate subscriptionStartDate,
        LocalDate subscriptionEndDate,
        Integer paidAmount,
        Integer usagePeriod
    ) {

        public static UserItem from(User user, UserSubscription sub) {
            String status = sub != null ? sub.getStatus().name() : "NO_SUBSCRIPTION";
            LocalDateTime paidAt = (sub != null && sub.getPayment() != null) ? sub.getPayment().getPaidAt() : null;
            LocalDate startDate = sub != null ? sub.getStartDate() : null;
            LocalDate endDate = sub != null ? sub.getEndDate() : null;
            Integer amount = (sub != null && sub.getPayment() != null) ? sub.getPayment().getTotalAmount() : null;
            Integer period = sub != null ? sub.getUsagePeriod() : null;
            return new UserItem(
                user.getUserId(),
                user.getLoginId(),
                user.getName(),
                user.getEmail(),
                status,
                paidAt,
                startDate,
                endDate,
                amount,
                period
            );
        }
    }
}

package com.verby.indp.domain.auth.dto.response;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.subscription.UserSubscription;

import java.time.LocalDate;
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
        LocalDate subscriptionEndDate
    ) {

        public static UserItem from(User user, UserSubscription sub) {
            String status = sub != null ? sub.getStatus().name() : "NO_SUBSCRIPTION";
            LocalDate endDate = sub != null ? sub.getEndDate() : null;
            return new UserItem(
                user.getUserId(),
                user.getLoginId(),
                user.getName(),
                user.getEmail(),
                status,
                endDate
            );
        }
    }
}

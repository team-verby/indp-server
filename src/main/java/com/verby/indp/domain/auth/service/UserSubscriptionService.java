package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.response.UserPaymentsResponse;
import com.verby.indp.domain.auth.dto.response.UserSubscriptionResponse;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSubscriptionService {

    private final UserSubscriptionRepository userSubscriptionRepository;

    public UserSubscriptionResponse getSubscription(User user) {
        UserSubscription subscription = userSubscriptionRepository
            .findTopByUserAndStatusOrderByCreatedAtDesc(user, UserSubscriptionStatus.ACTIVE)
            .orElseThrow(() -> new NotFoundException("구독 정보가 없습니다."));
        return UserSubscriptionResponse.from(subscription);
    }

    public UserPaymentsResponse getPayments(User user) {
        List<UserSubscription> subscriptions = userSubscriptionRepository
            .findAllByUserOrderByCreatedAtDesc(user);
        List<UserPaymentsResponse.PaymentItem> items = subscriptions.stream()
            .filter(s -> s.getPayment().getPaidAt() != null)
            .map(UserPaymentsResponse.PaymentItem::from)
            .toList();
        return new UserPaymentsResponse(items);
    }
}

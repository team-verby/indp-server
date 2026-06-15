package com.verby.indp.domain.auth.service;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.dto.response.FindUsersResponse;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional
    public FindUsersResponse findUsers() {
        List<User> users = userRepository.findAll();
        List<FindUsersResponse.UserItem> items = users.stream().map(user -> {
            List<UserSubscription> subs = userSubscriptionRepository.findAllByUserOrderByCreatedAtDesc(user);
            UserSubscription latest = subs.isEmpty() ? null : subs.get(0);
            // Eagerly access payment to avoid LazyInitializationException
            if (latest != null && latest.getPayment() != null) {
                latest.getPayment().getTotalAmount();
            }
            return FindUsersResponse.UserItem.from(user, latest);
        }).toList();
        return new FindUsersResponse(items);
    }
}

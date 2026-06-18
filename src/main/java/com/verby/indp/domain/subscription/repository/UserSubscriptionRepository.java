package com.verby.indp.domain.subscription.repository;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.subscription.UserSubscription;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    Optional<UserSubscription> findByPayment(Payment payment);

    Optional<UserSubscription> findTopByUserAndStatusOrderByCreatedAtDesc(
        User user, com.verby.indp.domain.subscription.UserSubscriptionStatus status);

    List<UserSubscription> findAllByUserOrderByCreatedAtDesc(User user);

    /** 기준일에 유효한 ACTIVE 구독이 있는지 (startDate ≤ date ≤ endDate). */
    boolean existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
        User user, UserSubscriptionStatus status, LocalDate startDate, LocalDate endDate);
}

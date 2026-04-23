package com.verby.indp.domain.subscription.repository;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StoreSubscriptionRepository extends JpaRepository<StoreSubscription, Long> {

    List<StoreSubscription> findAllByStore(Store store);

    Page<StoreSubscription> findAllByStoreOrderByPaymentCreatedAtDesc(Store store, Pageable pageable);

    Optional<StoreSubscription> findByPayment(Payment payment);

    List<StoreSubscription> findAllByStatusAndEndDateBefore(SubscriptionStatus status,
        LocalDate date);

    List<StoreSubscription> findAllByStatusAndStartDateLessThanEqual(SubscriptionStatus status,
        LocalDate date);
}

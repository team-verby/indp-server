package com.verby.indp.domain.subscription.repository;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.subscription.StoreSubscription;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreSubscriptionRepository extends JpaRepository<StoreSubscription, Long> {

    List<StoreSubscription> findAllByStore(Store store);

    Optional<StoreSubscription> findByPayment(Payment payment);
}

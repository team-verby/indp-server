package com.verby.indp.domain.store.repository;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(
        value = "SELECT DISTINCT s FROM Store s JOIN s.subscriptions sub WHERE sub.status = :status ORDER BY s.storeId ASC",
        countQuery = "SELECT COUNT(DISTINCT s) FROM Store s JOIN s.subscriptions sub WHERE sub.status = :status"
    )
    Page<Store> findAllBySubscriptionStatus(@Param("status") SubscriptionStatus status, Pageable pageable);

    Page<Store> findAllByOrderByStoreIdAsc(Pageable pageable);

    List<Store> findAllByOwner(Owner owner);
}

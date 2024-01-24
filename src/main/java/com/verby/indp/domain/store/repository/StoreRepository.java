package com.verby.indp.domain.store.repository;

import com.verby.indp.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    Page<Store> findAllByOrderByStoreIdAsc(Pageable pageable);
}

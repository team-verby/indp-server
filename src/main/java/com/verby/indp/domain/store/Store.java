package com.verby.indp.domain.store;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import com.verby.indp.domain.common.vo.Address;
import com.verby.indp.domain.store.vo.StoreName;
import com.verby.indp.domain.store.vo.StoreRegion;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private long storeId;

    @Embedded
    private StoreName name;

    @Embedded
    private Address address;

    @Embedded
    private StoreRegion region;

}

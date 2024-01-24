package com.verby.indp.domain.store.service;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public FindSimpleStoresResponse findSimpleStores(Pageable pageable) {
        Page<Store> page = storeRepository.findAllByOrderByStoreIdAsc(pageable);

        return FindSimpleStoresResponse.from(page);
    }

    public FindStoresResponse findStores(Pageable pageable, Region region) {
        if (region == null) {
            Page<Store> page = storeRepository.findAllByOrderByStoreIdAsc(
                pageable);

            return FindStoresResponse.from(page);
        }

        Page<Store> page = storeRepository.findAllByRegionOrderByStoreIdAsc(pageable, region);

        return FindStoresResponse.from(page);
    }
}

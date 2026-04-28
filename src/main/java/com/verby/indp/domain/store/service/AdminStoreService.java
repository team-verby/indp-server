package com.verby.indp.domain.store.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.playlist.service.CurrentSongResolver;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreStatus;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse.StoreItem;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStoreService {

    private final StoreRepository storeRepository;
    private final CurrentSongResolver currentSongResolver;

    public FindStoresByAdminResponse findStores(Pageable pageable) {
        Page<Store> storePage = storeRepository.findAllByStatusOrderByStoreIdAsc(StoreStatus.ACTIVE, pageable);
        List<StoreItem> items = storePage.getContent().stream()
            .map(store -> {
                CurrentSong currentSong = currentSongResolver.resolveCurrentSong(store).orElse(null);
                return StoreItem.from(store, currentSong);
            })
            .toList();
        return new FindStoresByAdminResponse(items, storePage.getTotalPages(), storePage.getTotalElements());
    }

    public FindStoreByAdminResponse findStore(long storeId) {
        Store store = getStoreById(storeId);
        return FindStoreByAdminResponse.from(store);
    }

    public Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }
}

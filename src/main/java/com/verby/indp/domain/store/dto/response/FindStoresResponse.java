package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.common.dto.PageInfo;
import com.verby.indp.domain.store.Store;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindStoresResponse(
    PageInfo pageInfo,
    List<StoreResponse> stores
) {

    private record StoreResponse(
        long id,
        String name,
        String address,
        String imageUrl,
        List<String> themes,
        List<String> songForms
    ) {

        private static StoreResponse from(Store store) {
            return new StoreResponse(
                store.getStoreId(),
                store.getName(),
                store.getAddress(),
                store.getImage().isEmpty() ? null : store.getImage().get(0),
                store.getThemes(),
                store.getSongForms()
            );
        }

    }

    public static FindStoresResponse from(Page<Store> page) {
        PageInfo pageInfo = new PageInfo(page.getTotalElements(), page.hasNext());
        List<StoreResponse> stores = page.get()
            .map(StoreResponse::from)
            .toList();

        return new FindStoresResponse(pageInfo, stores);
    }

}

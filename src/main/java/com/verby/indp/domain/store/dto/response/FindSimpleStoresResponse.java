package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.common.dto.PageInfo;
import com.verby.indp.domain.store.Store;
import java.util.List;
import org.springframework.data.domain.Page;

public record FindSimpleStoresResponse(
    PageInfo pageInfo,
    List<SimpleStoreResponse> stores
) {

    private record SimpleStoreResponse(
        long id,
        String name,
        String address,
        String imageUrl
    ) {

        private static SimpleStoreResponse from (Store store) {
            return new SimpleStoreResponse(
                store.getStoreId(),
                store.getName(),
                store.getAddress(),
                store.getImage().isEmpty() ? null : store.getImage().get(0)
            );
        }

    }

    public static FindSimpleStoresResponse from(Page<Store> page) {
        PageInfo pageInfo = new PageInfo(page.getTotalElements(), page.hasNext());
        List<SimpleStoreResponse> stores = page.get()
            .map(SimpleStoreResponse::from)
            .toList();

        return new FindSimpleStoresResponse(pageInfo, stores);
    }

}

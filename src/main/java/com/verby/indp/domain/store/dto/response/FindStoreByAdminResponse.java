package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import java.util.List;

public record FindStoreByAdminResponse(
    String name,
    String address,
    String region,
    String imageUrl,
    List<String> themes,
    List<String> songForms
) {

    public static FindStoreByAdminResponse from(Store store) {
        return new FindStoreByAdminResponse(
            store.getName(),
            store.getAddress(),
            store.getRegion(),
            store.getImage().isEmpty() ? null : store.getImage().get(0),
            store.getThemes(),
            store.getSongForms()
        );

    }

}

package com.verby.indp.domain.store.dto.request;

import java.util.List;

public record UpdateStoreByAdminRequest(
    String name,
    String region,
    String address,
    String imageUrl,
    List<String> themes,
    List<String> songForms
) {

}

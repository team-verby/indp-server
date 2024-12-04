package com.verby.indp.domain.store.dto.request;

import java.util.List;

public record AddStoreByAdminRequest(
    String name,
    String address,
    String region,
    String imageUrl,
    List<String> themes,
    List<String> songForms
) {

}

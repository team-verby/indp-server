package com.verby.indp.domain.store.dto.response;

public record ApplyStoreResponse(
    String orderId,
    int amount,
    String orderName
) {
}

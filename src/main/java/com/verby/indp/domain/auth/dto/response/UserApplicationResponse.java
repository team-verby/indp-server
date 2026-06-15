package com.verby.indp.domain.auth.dto.response;

public record UserApplicationResponse(
    String orderId,
    int amount,
    String orderName
) {

}

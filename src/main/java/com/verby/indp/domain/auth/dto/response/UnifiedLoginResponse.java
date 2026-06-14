package com.verby.indp.domain.auth.dto.response;

public record UnifiedLoginResponse(
    String accessToken,
    String refreshToken,
    String planType,
    Long storeId,
    String djName
) {

}

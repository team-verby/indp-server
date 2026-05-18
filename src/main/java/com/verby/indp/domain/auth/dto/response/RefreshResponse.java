package com.verby.indp.domain.auth.dto.response;

public record RefreshResponse(
    String accessToken,
    String refreshToken
) {

}

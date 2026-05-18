package com.verby.indp.domain.auth.dto.request;

public record LoginRequest(
    String loginId,
    String password
) {

}

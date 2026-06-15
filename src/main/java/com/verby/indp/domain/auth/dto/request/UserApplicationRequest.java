package com.verby.indp.domain.auth.dto.request;

public record UserApplicationRequest(
    String loginId,
    String name,
    String email,
    String password,
    int usagePeriod
) {

}

package com.verby.indp.domain.auth.dto.request;

public record UserApplicationRequest(
    String name,
    String email,
    String password,
    int usagePeriod
) {

}

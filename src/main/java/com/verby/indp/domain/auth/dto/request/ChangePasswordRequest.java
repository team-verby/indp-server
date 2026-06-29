package com.verby.indp.domain.auth.dto.request;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword
) {

}

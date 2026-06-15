package com.verby.indp.domain.creator.dto.request;

public record ChangePasswordRequest(
    String currentPassword,
    String newPassword
) {

}

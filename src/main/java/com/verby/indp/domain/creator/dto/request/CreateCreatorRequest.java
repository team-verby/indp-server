package com.verby.indp.domain.creator.dto.request;

public record CreateCreatorRequest(
    String name,
    String djName,
    String phone,
    String email,
    String password
) {

}

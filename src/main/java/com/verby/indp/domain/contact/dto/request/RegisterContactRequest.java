package com.verby.indp.domain.contact.dto.request;

public record RegisterContactRequest(
    String userName,
    String content,
    String phoneNumber
    ) {

}

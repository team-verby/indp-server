package com.verby.indp.domain.notification.dto;

public record ContactMail(
    String to,
    String content,
    String userName,
    String phoneNumber
) {

    public static ContactMail of(
        String to,
        String content,
        String userName,
        String phoneNumber
    ) {
        return new ContactMail(to, content, userName, phoneNumber);
    }

}

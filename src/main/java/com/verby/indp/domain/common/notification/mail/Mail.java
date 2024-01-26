package com.verby.indp.domain.common.notification.mail;

public record Mail(
    String to,
    String subject,
    String text
) {

}

package com.verby.indp.domain.notification.dto;

public record Mail(
    String to,
    String subject,
    String text
) {

}

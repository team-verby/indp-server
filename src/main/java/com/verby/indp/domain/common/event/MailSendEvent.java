package com.verby.indp.domain.common.event;

import com.verby.indp.domain.notification.dto.Mail;

public record MailSendEvent(
    Mail mail
) {
}

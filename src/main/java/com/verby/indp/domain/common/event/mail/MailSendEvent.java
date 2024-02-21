package com.verby.indp.domain.common.event.mail;

import com.verby.indp.domain.common.notification.mail.Mail;

public record MailSendEvent(
    Mail mail
) {
}

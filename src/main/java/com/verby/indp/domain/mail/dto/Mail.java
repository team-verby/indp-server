package com.verby.indp.domain.mail.dto;

import com.verby.indp.domain.notification.MailNotification;

public record Mail(
    long id,
    String to,
    String subject,
    String text
) {

    public static Mail from(MailNotification notification) {
        return new Mail(
            notification.getMailNotificationId(),
            notification.getReceiverEmail(),
            notification.getSubject(),
            notification.getText()
        );
    }

}

package com.verby.indp.domain.notification;

import com.verby.indp.domain.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "mail_notification")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MailNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_notification_id")
    private Long mailNotificationId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "receiver_email")
    private String receiverEmail;

    public MailNotification(String subject, String text, String receiverEmail) {
        this.subject = subject;
        this.text = text;
        this.receiverEmail = receiverEmail;
    }
}

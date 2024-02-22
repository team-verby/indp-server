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
@Table(name = "notification")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private long notificationId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "text")
    private String text;

    @Column(name = "receiver_email")
    private String receiverEmail;

    public Notification(String subject, String text, String receiverEmail) {
        this.subject = subject;
        this.text = text;
        this.receiverEmail = receiverEmail;
    }
}

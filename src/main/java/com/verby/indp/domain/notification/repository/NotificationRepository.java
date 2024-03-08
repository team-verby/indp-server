package com.verby.indp.domain.notification.repository;

import com.verby.indp.domain.notification.MailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<MailNotification, Long> {

}

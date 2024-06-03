package com.verby.indp.domain.notification.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.verby.indp.domain.contact.event.ContactMailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class ContactMailListener {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleContactMailEvent(ContactMailEvent event) {
        notificationService.sendContactMail(event.request());
    }

}

package com.verby.indp.domain.mail.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.verby.indp.domain.notification.event.SendMailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class SendMailListener {

    private final MailService mailService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleSendMailEvent(SendMailEvent event) {
        mailService.sendMail(event.mail());
    }
}

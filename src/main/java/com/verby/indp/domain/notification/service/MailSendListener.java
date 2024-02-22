package com.verby.indp.domain.notification.service;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.verby.indp.domain.common.event.MailSendEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class MailSendListener {

    private final MailService mailService;

    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleMailSendEvent(MailSendEvent event) {
        mailService.sendMail(event.mail());
    }

}
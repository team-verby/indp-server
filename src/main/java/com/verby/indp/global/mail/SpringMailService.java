package com.verby.indp.global.mail;

import com.verby.indp.domain.mail.dto.Mail;
import com.verby.indp.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringMailService implements MailService {

    private static final int MAX_RETRY_COUNT = 5;

    private final MailSender mailSender;

    @Override
    public void sendMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mail.to());
        message.setSubject(mail.subject());
        message.setText(mail.text());

        send(mail.id(), message);
    }

    private void send(long id, SimpleMailMessage message) {
        for (int count = 0; count < MAX_RETRY_COUNT; count++) {
            try {
                mailSender.send(message);
                return;
            } catch (MailParseException | MailAuthenticationException exception) {
                log.error("메일 전송을 실패하였습니다. mailNotificationId: {}", id, exception);
                return;
            } catch (MailSendException exception) {
                log.warn("메일 전송을 실패하였습니다. 재시도합니다. mailNotificationId: {} 재시도 횟수: {}", id, count, exception);
            }
        }
        log.error("메일 전송에 실패하였습니다. mailNotificationId: {} 재시도 횟수: {}", id, MAX_RETRY_COUNT);
    }
}

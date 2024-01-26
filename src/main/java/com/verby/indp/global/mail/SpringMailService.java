package com.verby.indp.global.mail;

import com.verby.indp.domain.common.notification.mail.MailService;
import com.verby.indp.domain.common.notification.mail.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringMailService implements MailService {

    private final MailSender mailSender;

    @Override
    public void sendMail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mail.to());
        message.setSubject(mail.subject());
        message.setText(mail.text());

        mailSender.send(message);
    }
}

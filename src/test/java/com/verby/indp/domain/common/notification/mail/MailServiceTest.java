package com.verby.indp.domain.common.notification.mail;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.verby.indp.global.mail.SpringMailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    private SpringMailService mailService;

    @Mock
    private MailSender mailSender;

    @Nested
    @DisplayName("sendMail 메소드 실행 시")
    class SendMail {

        @Test
        @DisplayName("성공: 메일을 전송한다.")
        void sendMail() {
            // given
            Mail mail = new Mail("to", "subject", "text");

            // when
            mailService.sendMail(mail);

            // then
            verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        }
    }

}

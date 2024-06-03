package com.verby.indp.domain.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.notification.MailNotification;
import com.verby.indp.domain.notification.dto.ContactMail;
import com.verby.indp.domain.notification.dto.RecommendationMail;
import com.verby.indp.domain.notification.event.SendMailEvent;
import com.verby.indp.domain.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Nested
    @DisplayName("sendContactMail 메소드 실행 시")
    class SendContactMail {

        @Test
        @DisplayName("성공: 문의 메일을 전송한다.")
        void sendContactMail() {
            // given
            ContactMail contactMail = new ContactMail("userName", "content", "phoneNumber", "to");
            MailNotification mailNotification = new MailNotification("subject", "text", "to");
            ReflectionTestUtils.setField(mailNotification, "mailNotificationId", 1L);

            when(notificationRepository.save(any(MailNotification.class))).thenReturn(mailNotification);

            // when
            notificationService.sendContactMail(contactMail);

            // then
            verify(notificationRepository, times(1)).save(any(MailNotification.class));
            verify(applicationEventPublisher, times(1)).publishEvent(any(SendMailEvent.class));
        }

    }

    @Nested
    @DisplayName("sendRecommendationMail 메소드 실행 시")
    class SendRecommendationMail {

        @Test
        @DisplayName("성공: 문의 메일을 전송한다.")
        void sendContactMail() {
            // given
            RecommendationMail recommendationMail = new RecommendationMail("to", "information",
                "01012341234", "storeName", "storeAddress");
            MailNotification mailNotification = new MailNotification("subject", "text", "to");
            ReflectionTestUtils.setField(mailNotification, "mailNotificationId", 1L);

            when(notificationRepository.save(any(MailNotification.class))).thenReturn(mailNotification);

            // when
            notificationService.sendRecommendationMail(recommendationMail);

            // then
            verify(notificationRepository, times(1)).save(any(MailNotification.class));
            verify(applicationEventPublisher, times(1)).publishEvent(any(SendMailEvent.class));
        }

    }

}
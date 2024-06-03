package com.verby.indp.domain.notification.service;

import com.verby.indp.domain.notification.MailNotification;
import com.verby.indp.domain.mail.dto.Mail;
import com.verby.indp.domain.notification.dto.ContactMail;
import com.verby.indp.domain.notification.dto.RecommendationMail;
import com.verby.indp.domain.notification.event.SendMailEvent;
import com.verby.indp.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void sendContactMail(ContactMail request) {
        MailNotification mailNotification = new MailNotification("[버비] 문의가 들어왔어요!",
            "문의 내용: " + request.content() + "\n" +
                "문의자 성함: " + request.userName() + "\n" +
                "문의자 연락처: " + request.phoneNumber() + "\n", request.to());
        MailNotification persistMailNotification = notificationRepository.save(mailNotification);
        sendMail(persistMailNotification);
    }

    @Transactional
    public void sendRecommendationMail(RecommendationMail request) {
        MailNotification mailNotification = new MailNotification("[버비] 인디피 서비스에 음악이 추천되었어요!",
            "추천 음악 정보: " + request.information() + "\n" +
                "추천인 연락처: " + request.phoneNumber() + "\n" +
                "매장 이름: " + request.storeName() + "\n" +
                "매장 주소: " + request.storeAddress() + "\n", request.to());
        MailNotification persistMailNotification = notificationRepository.save(mailNotification);
        sendMail(persistMailNotification);
    }

    private void sendMail(MailNotification persistMailNotification) {
        Mail mail = Mail.from(persistMailNotification);
        applicationEventPublisher.publishEvent(new SendMailEvent(mail));
    }

}

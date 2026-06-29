package com.verby.indp.global.notification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 도입 문의(매장·Plan A) 접수 시 운영 메일로 알림을 발송한다.
 * 결제 모듈 점검(계좌이체) 기간에는 결제확인 단계가 없어 슬랙 알림이 발송되지 않으므로,
 * 신청 시점에 본 메일 알림으로 접수 사실을 통지한다.
 * 메일 발송 실패가 신청 처리에 영향을 주지 않도록 예외는 로깅 후 무시한다.
 */
@Service
@RequiredArgsConstructor
public class ApplicationMailService {

    private static final Logger log = LoggerFactory.getLogger(ApplicationMailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.application-notification.recipient:verbykorea@gmail.com}")
    private String recipientEmail;

    public void notifyStoreApplication(String storeName, String applicantName, String applicantPhone,
        Long planId, int usagePeriod, String loginId, String password) {
        String body = """
            새로운 매장 도입 문의가 접수되었습니다.

            ▶ 매장명: %s
            ▶ 신청자: %s
            ▶ 연락처: %s
            ▶ 요금제(planId): %s
            ▶ 이용 기간: %d개월
            ▶ 발급 계정: %s / %s

            ※ 결제는 계좌이체로 진행됩니다. 입금 확인 후 구독을 수동 활성화해야 매장이 노출·재생됩니다.
            """.formatted(storeName, applicantName, applicantPhone, planId, usagePeriod, loginId, password);
        send("[인디피뮤직] 매장 도입 문의 - " + storeName, body);
    }

    public void notifyUserApplication(String loginId, String name, String email, int usagePeriod,
        int amount) {
        String body = """
            새로운 Plan A 라이트 구독 신청이 접수되었습니다.

            ▶ 신청자: %s
            ▶ 이메일: %s
            ▶ 아이디: %s
            ▶ 구독: %s (%,d원)

            ※ 결제는 계좌이체로 진행됩니다. 입금 확인 후 구독을 수동 활성화해야 서비스가 시작됩니다.
            """.formatted(name, email, loginId, usagePeriod == 12 ? "연간" : "월간", amount);
        send("[인디피뮤직] Plan A 라이트 신청 - " + name, body);
    }

    private void send(String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(recipientEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("도입 신청 알림 메일 발송 실패: {}", subject, e);
        }
    }
}

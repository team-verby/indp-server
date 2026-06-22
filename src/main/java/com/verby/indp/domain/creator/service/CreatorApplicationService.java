package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.dto.request.CreatorApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * DJ 크리에이터 지원 접수. 제출 내용을 운영 메일로 발송한다.
 * 개인정보 수집·이용 동의가 없으면 접수하지 않으며, 별도 저장은 하지 않는다.
 */
@Service
@RequiredArgsConstructor
public class CreatorApplicationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.creator-application.recipient:verbykorea@gmail.com}")
    private String recipientEmail;

    public void apply(CreatorApplicationRequest request) {
        if (!request.privacyConsent()) {
            throw new BadRequestException("개인정보 수집·이용에 동의해야 지원할 수 있습니다.");
        }
        if (!StringUtils.hasText(request.name())
            || !StringUtils.hasText(request.phone())
            || !StringUtils.hasText(request.email())
            || !StringUtils.hasText(request.youtubeUrl())) {
            throw new BadRequestException("이름, 연락처, 이메일, 유튜브 플레이리스트 URL은 필수입니다.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(recipientEmail);
        message.setReplyTo(request.email());
        message.setSubject("[인디피뮤직] DJ 크리에이터 지원 - " + request.name());
        message.setText(buildBody(request));
        mailSender.send(message);
    }

    private String buildBody(CreatorApplicationRequest request) {
        String introduction = StringUtils.hasText(request.introduction())
            ? request.introduction() : "(미입력)";
        return """
            DJ 크리에이터 지원이 접수되었습니다.

            ▶ 이름: %s
            ▶ 연락처: %s
            ▶ 이메일: %s
            ▶ 유튜브 플레이리스트: %s
            ▶ 소개: %s

            ※ 본 메일은 메인 페이지 크리에이터 모집 배너를 통해 자동 발송되었습니다.
            ※ 지원자가 개인정보 수집·이용에 동의했습니다.
            """.formatted(
            request.name(),
            request.phone(),
            request.email(),
            request.youtubeUrl(),
            introduction
        );
    }
}

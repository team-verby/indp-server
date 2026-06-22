package com.verby.indp.domain.creator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.dto.request.CreatorApplicationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class CreatorApplicationServiceTest {

    @InjectMocks
    private CreatorApplicationService creatorApplicationService;

    @Mock
    private JavaMailSender mailSender;

    @Nested
    @DisplayName("apply 메서드 실행 시")
    class Apply {

        @Test
        @DisplayName("성공 : 동의·필수값이 채워지면 운영 메일을 발송한다.")
        void apply() {
            // given
            CreatorApplicationRequest request = new CreatorApplicationRequest(
                "박완", "010-1234-5678", "dj@example.com",
                "https://youtube.com/playlist?list=abc", "신나는 음악", true);

            // when
            Exception exception = catchException(() -> creatorApplicationService.apply(request));

            // then
            assertThat(exception).isNull();
            then(mailSender).should(times(1)).send(any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("성공 : 소개가 비어 있어도 발송한다.")
        void applyWithoutIntroduction() {
            // given
            CreatorApplicationRequest request = new CreatorApplicationRequest(
                "박완", "010-1234-5678", "dj@example.com",
                "https://youtube.com/playlist?list=abc", "  ", true);

            // when
            creatorApplicationService.apply(request);

            // then
            then(mailSender).should(times(1)).send(any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("실패 : 개인정보 동의가 없으면 예외를 던지고 발송하지 않는다.")
        void applyWithoutConsent() {
            // given
            CreatorApplicationRequest request = new CreatorApplicationRequest(
                "박완", "010-1234-5678", "dj@example.com",
                "https://youtube.com/playlist?list=abc", "신나는 음악", false);

            // when
            Exception exception = catchException(() -> creatorApplicationService.apply(request));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
            then(mailSender).should(never()).send(any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("실패 : 필수값이 비면 예외를 던지고 발송하지 않는다.")
        void applyWithBlankRequired() {
            // given
            CreatorApplicationRequest request = new CreatorApplicationRequest(
                "박완", "", "dj@example.com",
                "https://youtube.com/playlist?list=abc", "신나는 음악", true);

            // when
            Exception exception = catchException(() -> creatorApplicationService.apply(request));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
            then(mailSender).should(never()).send(any(SimpleMailMessage.class));
        }
    }
}

package com.verby.indp.domain.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Nested
    @DisplayName("Notification 생성 시")
    class NewNotification {

        @Test
        @DisplayName("성공: Notification 을 생성한다.")
        void newNotification() {
            // given
            String to = "email@email.com";
            String subject = "subject";
            String text = "text";

            // when
            Exception exception = catchException(
                () -> new MailNotification(to, subject, text));

            // then
            assertThat(exception).isNull();
        }

    }

}

package com.verby.indp.domain.contact.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContactUserNameTest {

    @Nested
    @DisplayName("ContactUserName 생성 시")
    class NewContactUserName {

        @Test
        @DisplayName("성공: ContactUserName 을 생성한다.")
        void newContactUserName() {
            // given
            String userName = "김버비";

            // when
            Exception exception = catchException(() -> new ContactUserName(userName));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외: userName 이 null 이라면 예외가 발생한다.")
        void exceptionWhenUserNameIsNull() {
            // given
            String nullUserName = null;

            // when
            Exception exception = catchException(() -> new ContactUserName(nullUserName));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: userName 가 비어있다면 이라면 예외가 발생한다.")
        void exceptionWhenUserNameIsEmpty() {
            // given
            String username = "";

            // when
            Exception exception = catchException(() -> new ContactUserName(username));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: username 이 공백 이라면 예외가 발생한다.")
        void exceptionWhenUserNameIsBlank() {
            // given
            String username = "       ";

            // when
            Exception exception = catchException(() -> new ContactUserName(username));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: username 의 크기가 50자를 초과하면 예외가 발생한다.")
        void exceptionWhenUserNameIsOverSize() {
            // given
            int maxUserNameSize = 50;
            String userName = "버".repeat(maxUserNameSize + 1);

            // when
            Exception exception = catchException(() -> new ContactUserName(userName));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }
    }

}

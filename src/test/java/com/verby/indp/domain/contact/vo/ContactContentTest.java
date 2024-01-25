package com.verby.indp.domain.contact.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ContactContentTest {

    @Nested
    @DisplayName("ContactContent 생성 시")
    class NewContactContent{

        @Test
        @DisplayName("성공: ContactContent 을 생성한다.")
        void newContactContent() {
            // given
            String content = "버비버비버비";

            // when
            Exception exception = catchException(() -> new ContactContent(content));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외: content 이 null 이라면 예외가 발생한다.")
        void exceptionWhenContentIsNull() {
            // given
            String content = null;

            // when
            Exception exception = catchException(() -> new ContactContent(content));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: content 가 비어있다면 이라면 예외가 발생한다.")
        void exceptionWhenContentIsEmpty() {
            // given
            String content = "";

            // when
            Exception exception = catchException(() -> new ContactContent(content));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: content 이 공백 이라면 예외가 발생한다.")
        void exceptionWhenContentIsBlank() {
            // given
            String content = "       ";

            // when
            Exception exception = catchException(() -> new ContactContent(content));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: content 의 크기가 150자를 초과하면 예외가 발생한다.")
        void exceptionWhenContentIsOverSize() {
            // given
            int maxContentSize = 150;
            String content = "버".repeat(maxContentSize + 1);

            // when
            Exception exception = catchException(() -> new ContactContent(content));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }
    }

}

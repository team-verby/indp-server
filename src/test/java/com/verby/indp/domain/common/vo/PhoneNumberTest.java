package com.verby.indp.domain.common.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PhoneNumberTest {

    @Nested
    @DisplayName("PhoneNumber 생성 시")
    class NewPhoneNumber {

        @Test
        @DisplayName("성공: PhoneNumber 를 생성한다.")
        void newPhoneNumber() {
            // given
            String phoneNumber = "01012345678";

            // when
            Exception exception = catchException(() -> new PhoneNumber(phoneNumber));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외: PhoneNumber 가 null 이면 예외가 발생한다.")
        void exceptionWhenPhoneNumberIsNull() {
            // given
            String nullPhoneNumber = null;

            // when
            Exception exception = catchException(() -> new PhoneNumber(nullPhoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("예외: PhoneNumber 가 비어있으면 예외가 발생한다.")
        void exceptionWhenPhoneNumberIsEmpty() {
            // given
            String nullPhoneNumber = "";

            // when
            Exception exception = catchException(() -> new PhoneNumber(nullPhoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("예외: PhoneNumber 가 공백이라면 예외가 발생한다.")
        void exceptionWhenPhoneNumberIsBlank() {
            // given
            String nullPhoneNumber = "      ";

            // when
            Exception exception = catchException(() -> new PhoneNumber(nullPhoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("예외: PhoneNumber 가 숫자가 아니라면 예외가 발생한다.")
        void exceptionWhenPhoneNumberIsNotNumber() {
            // given
            String nullPhoneNumber = "010aaaaaab";

            // when
            Exception exception = catchException(() -> new PhoneNumber(nullPhoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("예외: PhoneNumber 가 최대 크기를 초과한다면 예외가 발생한다.")
        void exceptionWhenPhoneNumberIsOverSize() {
            // given
            int maxPhoneNumberSize = 50;
            String nullPhoneNumber = "1".repeat(maxPhoneNumberSize + 1);

            // when
            Exception exception = catchException(() -> new PhoneNumber(nullPhoneNumber));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }

}

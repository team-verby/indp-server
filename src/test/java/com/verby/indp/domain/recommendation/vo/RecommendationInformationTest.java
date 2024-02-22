package com.verby.indp.domain.recommendation.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RecommendationInformationTest {

    @Nested
    @DisplayName("RecommendationInformation 생성 시")
    class NewRecommendationInformation {

        @Test
        @DisplayName("성공: RecommendationInformation 을 생성한다.")
        void newRecommendationInformation() {
            // given
            String information = "잔나비-주저하는 연인들을 위해";

            // when
            Exception exception = catchException(() -> new RecommendationInformation(information));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("예외: information 이 null 이라면 예외가 발생한다.")
        void exceptionWhenInformationIsNull() {
            // given
            String nullInformation = null;

            // when
            Exception exception = catchException(() -> new RecommendationInformation(nullInformation));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: information 가 비어있다면 이라면 예외가 발생한다.")
        void exceptionWhenInformationIsEmpty() {
            // given
            String nullInformation = "";

            // when
            Exception exception = catchException(() -> new RecommendationInformation(nullInformation));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: information 이 공백 이라면 예외가 발생한다.")
        void exceptionWhenInformationIsBlank() {
            // given
            String nullInformation = "       ";

            // when
            Exception exception = catchException(() -> new RecommendationInformation(nullInformation));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("예외: information 의 크기가 50자를 초과하면 예외가 발생한다.")
        void exceptionWhenInformationIsOverSize() {
            // given
            int maxInformationSize = 50;
            String nullInformation = "완".repeat(maxInformationSize + 1);

            // when
            Exception exception = catchException(() -> new RecommendationInformation(nullInformation));

            // then
            assertThat(exception).isInstanceOf(IllegalArgumentException.class);
        }
    }

}

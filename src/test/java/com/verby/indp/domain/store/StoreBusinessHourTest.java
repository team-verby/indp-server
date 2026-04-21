package com.verby.indp.domain.store;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.fixture.StoreFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

class StoreBusinessHourTest {

    private Store store() {
        return StoreFixture.store();
    }

    @Nested
    @DisplayName("StoreBusinessHour 생성 시")
    class NewStoreBusinessHour {

        @Test
        @DisplayName("성공 : 영업일 StoreBusinessHour를 생성한다.")
        void newStoreBusinessHour() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, LocalTime.of(10, 0), LocalTime.of(22, 0), false));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("성공 : 휴무일 StoreBusinessHour를 생성한다.")
        void newStoreBusinessHourWithClosed() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, null, null, true));

            // then
            assertThat(exception).isNull();
        }

        @Test
        @DisplayName("실패 : store가 null이면 예외를 던진다.")
        void newStoreBusinessHourWithNullStore() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(null, 1, LocalTime.of(10, 0), LocalTime.of(22, 0), false));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : dayOfWeek가 1 미만이면 예외를 던진다.")
        void newStoreBusinessHourWithDayOfWeekUnder1() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 0, LocalTime.of(10, 0), LocalTime.of(22, 0), false));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : dayOfWeek가 7 초과이면 예외를 던진다.")
        void newStoreBusinessHourWithDayOfWeekOver7() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 8, LocalTime.of(10, 0), LocalTime.of(22, 0), false));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 영업일인데 openTime이 null이면 예외를 던진다.")
        void newStoreBusinessHourWithNullOpenTime() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, null, LocalTime.of(22, 0), false));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 영업일인데 closeTime이 null이면 예외를 던진다.")
        void newStoreBusinessHourWithNullCloseTime() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, LocalTime.of(10, 0), null, false));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 휴무일인데 openTime이 있으면 예외를 던진다.")
        void newStoreBusinessHourWithClosedAndOpenTime() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, LocalTime.of(10, 0), null, true));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 휴무일인데 closeTime이 있으면 예외를 던진다.")
        void newStoreBusinessHourWithClosedAndCloseTime() {
            // when
            Exception exception = catchException(() ->
                new StoreBusinessHour(store(), 1, null, LocalTime.of(22, 0), true));

            // then
            assertThat(exception).isInstanceOf(BadRequestException.class);
        }
    }
}

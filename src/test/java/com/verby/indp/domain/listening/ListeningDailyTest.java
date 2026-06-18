package com.verby.indp.domain.listening;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ListeningDailyTest {

    private static final LocalDate YMD = LocalDate.of(2026, 6, 18);

    @Nested
    @DisplayName("생성 시")
    class Create {

        @Test
        @DisplayName("성공 : 유효한 값으로 생성한다.")
        void create() {
            ListeningDaily daily = new ListeningDaily(1L, 10L, YMD, 60);

            assertThat(daily.getUserId()).isEqualTo(1L);
            assertThat(daily.getCreatorId()).isEqualTo(10L);
            assertThat(daily.getYmd()).isEqualTo(YMD);
            assertThat(daily.getSeconds()).isEqualTo(60);
        }

        @Test
        @DisplayName("실패 : userId가 없으면 예외를 던진다.")
        void createWithoutUserId() {
            Exception ex = catchException(() -> new ListeningDaily(null, 10L, YMD, 60));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : creatorId가 없으면 예외를 던진다.")
        void createWithoutCreatorId() {
            Exception ex = catchException(() -> new ListeningDaily(1L, null, YMD, 60));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : ymd가 없으면 예외를 던진다.")
        void createWithoutYmd() {
            Exception ex = catchException(() -> new ListeningDaily(1L, 10L, null, 60));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : seconds가 음수면 예외를 던진다.")
        void createWithNegativeSeconds() {
            Exception ex = catchException(() -> new ListeningDaily(1L, 10L, YMD, -1));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("addSeconds 메서드 실행 시")
    class AddSeconds {

        @Test
        @DisplayName("성공 : 델타를 누적한다.")
        void accumulates() {
            ListeningDaily daily = new ListeningDaily(1L, 10L, YMD, 100);

            daily.addSeconds(200, SettlementPolicy.DAILY_CAP_SEC);

            assertThat(daily.getSeconds()).isEqualTo(300);
        }

        @Test
        @DisplayName("성공 : 일일 상한을 초과하면 상한으로 클램프한다.")
        void clampsToDailyCap() {
            ListeningDaily daily = new ListeningDaily(1L, 10L, YMD, SettlementPolicy.DAILY_CAP_SEC);

            daily.addSeconds(300, SettlementPolicy.DAILY_CAP_SEC);

            assertThat(daily.getSeconds()).isEqualTo(SettlementPolicy.DAILY_CAP_SEC);
        }

        @Test
        @DisplayName("무시 : 0 이하 델타는 누적하지 않는다.")
        void ignoresNonPositive() {
            ListeningDaily daily = new ListeningDaily(1L, 10L, YMD, 100);

            daily.addSeconds(0, SettlementPolicy.DAILY_CAP_SEC);
            daily.addSeconds(-50, SettlementPolicy.DAILY_CAP_SEC);

            assertThat(daily.getSeconds()).isEqualTo(100);
        }
    }
}

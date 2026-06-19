package com.verby.indp.domain.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.verby.indp.domain.common.exception.BadRequestException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SettlementRequestTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 18, 10, 0);

    @Nested
    @DisplayName("생성 시")
    class Create {

        @Test
        @DisplayName("성공 : REQUESTED 상태로 생성된다.")
        void create() {
            SettlementRequest request = new SettlementRequest(10L, 60_000L, NOW);

            assertThat(request.getStatus()).isEqualTo(SettlementStatus.REQUESTED);
            assertThat(request.getAmount()).isEqualTo(60_000L);
            assertThat(request.getProcessedAt()).isNull();
        }

        @Test
        @DisplayName("실패 : creatorId가 없으면 예외를 던진다.")
        void createWithoutCreatorId() {
            assertThatThrownBy(() -> new SettlementRequest(null, 60_000L, NOW))
                .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : 금액이 0 이하면 예외를 던진다.")
        void createWithNonPositiveAmount() {
            assertThatThrownBy(() -> new SettlementRequest(10L, 0L, NOW))
                .isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : requestedAt이 없으면 예외를 던진다.")
        void createWithoutRequestedAt() {
            assertThatThrownBy(() -> new SettlementRequest(10L, 60_000L, null))
                .isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("상태 전이 시")
    class Transition {

        @Test
        @DisplayName("성공 : markPaid는 PAID로 전환하고 처리 일시를 기록한다.")
        void markPaid() {
            SettlementRequest request = new SettlementRequest(10L, 60_000L, NOW);

            request.markPaid(NOW.plusDays(1));

            assertThat(request.getStatus()).isEqualTo(SettlementStatus.PAID);
            assertThat(request.getProcessedAt()).isEqualTo(NOW.plusDays(1));
        }

        @Test
        @DisplayName("성공 : markRejected는 REJECTED로 전환한다.")
        void markRejected() {
            SettlementRequest request = new SettlementRequest(10L, 60_000L, NOW);

            request.markRejected(NOW.plusDays(1));

            assertThat(request.getStatus()).isEqualTo(SettlementStatus.REJECTED);
        }

        @Test
        @DisplayName("실패 : 이미 처리된 신청은 다시 처리할 수 없다.")
        void cannotReprocess() {
            SettlementRequest request = new SettlementRequest(10L, 60_000L, NOW);
            request.markPaid(NOW.plusDays(1));

            assertThatThrownBy(() -> request.markRejected(NOW.plusDays(2)))
                .isInstanceOf(BadRequestException.class);
        }
    }
}

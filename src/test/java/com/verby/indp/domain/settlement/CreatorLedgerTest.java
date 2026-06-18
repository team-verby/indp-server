package com.verby.indp.domain.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreatorLedgerTest {

    @Nested
    @DisplayName("생성 시")
    class Create {

        @Test
        @DisplayName("성공 : 적립 원장을 생성한다.")
        void create() {
            CreatorLedger ledger = new CreatorLedger(1L, LedgerType.ACCRUAL, 500, 500, "2026-06");

            assertThat(ledger.getCreatorId()).isEqualTo(1L);
            assertThat(ledger.getType()).isEqualTo(LedgerType.ACCRUAL);
            assertThat(ledger.getAmount()).isEqualTo(500);
            assertThat(ledger.getBalanceAfter()).isEqualTo(500);
            assertThat(ledger.getRef()).isEqualTo("2026-06");
        }

        @Test
        @DisplayName("실패 : creatorId가 없으면 예외를 던진다.")
        void createWithoutCreatorId() {
            Exception ex = catchException(
                () -> new CreatorLedger(null, LedgerType.ACCRUAL, 500, 500, "2026-06"));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }

        @Test
        @DisplayName("실패 : type이 없으면 예외를 던진다.")
        void createWithoutType() {
            Exception ex = catchException(
                () -> new CreatorLedger(1L, null, 500, 500, "2026-06"));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }
    }
}

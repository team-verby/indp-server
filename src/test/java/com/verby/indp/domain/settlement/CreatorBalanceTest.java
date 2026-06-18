package com.verby.indp.domain.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

import com.verby.indp.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreatorBalanceTest {

    @Nested
    @DisplayName("생성 시")
    class Create {

        @Test
        @DisplayName("성공 : 잔액 0으로 생성한다.")
        void create() {
            CreatorBalance balance = new CreatorBalance(1L);
            assertThat(balance.getCreatorId()).isEqualTo(1L);
            assertThat(balance.getBalance()).isZero();
        }

        @Test
        @DisplayName("실패 : creatorId가 없으면 예외를 던진다.")
        void createWithoutCreatorId() {
            Exception ex = catchException(() -> new CreatorBalance(null));
            assertThat(ex).isInstanceOf(BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("apply 메서드 실행 시")
    class Apply {

        @Test
        @DisplayName("성공 : 적립(+)을 반영하고 반영 후 잔액을 반환한다.")
        void accrue() {
            CreatorBalance balance = new CreatorBalance(1L);

            long after = balance.apply(500);

            assertThat(after).isEqualTo(500);
            assertThat(balance.getBalance()).isEqualTo(500);
        }

        @Test
        @DisplayName("성공 : 출금(−)을 반영한다.")
        void payout() {
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(1000);

            long after = balance.apply(-300);

            assertThat(after).isEqualTo(700);
        }

        @Test
        @DisplayName("실패 : 잔액보다 큰 출금은 예외를 던진다.")
        void payoutOverBalance() {
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(100);

            Exception ex = catchException(() -> balance.apply(-200));

            assertThat(ex).isInstanceOf(BadRequestException.class);
        }
    }
}

package com.verby.indp.domain.settlement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.CreatorLedger;
import com.verby.indp.domain.settlement.LedgerType;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.CreatorLedgerRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatorAccrualServiceTest {

    @InjectMocks
    private CreatorAccrualService creatorAccrualService;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private ListeningDailyRepository listeningDailyRepository;

    @Mock
    private CreatorBalanceRepository creatorBalanceRepository;

    @Mock
    private CreatorLedgerRepository creatorLedgerRepository;

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("accrueForMonth 메서드 실행 시")
    class AccrueForMonth {

        private final YearMonth month = YearMonth.of(2026, 6);

        @Test
        @DisplayName("성공 : 전월 청취시간을 단가로 환산해 잔액과 원장에 적립한다.")
        void accrue() {
            given(creatorRepository.findActiveCreatorIds()).willReturn(List.of(1L));
            given(creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                1L, LedgerType.ACCRUAL, "2026-06")).willReturn(false);
            // 345,600초 / 3456 = 100원
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(345_600L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.empty());
            given(creatorBalanceRepository.save(any()))
                .willAnswer(inv -> inv.getArgument(0));

            creatorAccrualService.accrueForMonth(month);

            ArgumentCaptor<CreatorLedger> captor = ArgumentCaptor.forClass(CreatorLedger.class);
            verify(creatorLedgerRepository).save(captor.capture());
            CreatorLedger ledger = captor.getValue();
            assertThat(ledger.getType()).isEqualTo(LedgerType.ACCRUAL);
            assertThat(ledger.getAmount()).isEqualTo(100L);
            assertThat(ledger.getBalanceAfter()).isEqualTo(100L);
            assertThat(ledger.getRef()).isEqualTo("2026-06");
        }

        @Test
        @DisplayName("멱등 : 이미 해당 월 적립이 있으면 건너뛴다.")
        void skipsWhenAlreadyAccrued() {
            given(creatorRepository.findActiveCreatorIds()).willReturn(List.of(1L));
            given(creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                1L, LedgerType.ACCRUAL, "2026-06")).willReturn(true);

            creatorAccrualService.accrueForMonth(month);

            verify(listeningDailyRepository, never()).sumSeconds(any(), any(), any());
            verify(creatorLedgerRepository, never()).save(any());
        }

        @Test
        @DisplayName("스킵 : 환산 적립액이 0원이면 적립하지 않는다.")
        void skipsWhenZeroWon() {
            given(creatorRepository.findActiveCreatorIds()).willReturn(List.of(1L));
            given(creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                1L, LedgerType.ACCRUAL, "2026-06")).willReturn(false);
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(100L); // <3456

            creatorAccrualService.accrueForMonth(month);

            verify(creatorLedgerRepository, never()).save(any());
            verify(creatorBalanceRepository, never()).save(any());
        }

        @Test
        @DisplayName("성공 : 기존 잔액이 있으면 그 위에 누적한다.")
        void accrueOnExistingBalance() {
            given(creatorRepository.findActiveCreatorIds()).willReturn(List.of(1L));
            given(creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                1L, LedgerType.ACCRUAL, "2026-06")).willReturn(false);
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(345_600L);
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(50_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));

            creatorAccrualService.accrueForMonth(month);

            assertThat(balance.getBalance()).isEqualTo(50_100L);
            verify(creatorBalanceRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("accruePreviousMonth 메서드 실행 시")
    class AccruePreviousMonth {

        @Test
        @DisplayName("성공 : 직전 달을 적립 대상 월로 사용한다.")
        void accruePreviousMonth() {
            given(clock.instant()).willReturn(Instant.parse("2026-07-10T03:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));
            given(creatorRepository.findActiveCreatorIds()).willReturn(List.of(1L));
            given(creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                1L, LedgerType.ACCRUAL, "2026-06")).willReturn(true);

            creatorAccrualService.accruePreviousMonth();

            verify(creatorLedgerRepository)
                .existsByCreatorIdAndTypeAndRef(1L, LedgerType.ACCRUAL, "2026-06");
        }
    }
}

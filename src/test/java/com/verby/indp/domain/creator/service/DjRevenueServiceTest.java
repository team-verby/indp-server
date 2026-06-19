package com.verby.indp.domain.creator.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.SettlementRequestRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DjRevenueServiceTest {

    @InjectMocks
    private DjRevenueService djRevenueService;

    @Mock
    private ListeningDailyRepository listeningDailyRepository;

    @Mock
    private CreatorBalanceRepository creatorBalanceRepository;

    @Mock
    private SettlementRequestRepository settlementRequestRepository;

    @Mock
    private Clock clock;

    private void fixToday() {
        given(clock.instant()).willReturn(Instant.parse("2026-06-18T00:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("UTC"));
    }

    @Nested
    @DisplayName("getRevenue 메서드 실행 시")
    class GetRevenue {

        @Test
        @DisplayName("성공 : 당월 예상 적립·잔액·신청 가능 여부를 계산한다.")
        void getRevenue() {
            Creator creator = creatorWithId(1L);
            fixToday();
            // 당월 청취 345,600초 → /3456 = 100원
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(345_600L);
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(60_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));
            given(settlementRequestRepository.existsByCreatorIdAndStatus(
                1L, SettlementStatus.REQUESTED)).willReturn(false);

            DjRevenueResponse response = djRevenueService.getRevenue(creator);

            assertThat(response.thisMonthEstimate()).isEqualTo(100L);
            assertThat(response.balance()).isEqualTo(60_000L);
            assertThat(response.totalPaid()).isEqualTo(0L);
            assertThat(response.canRequest()).isTrue(); // 60,000 ≥ 50,000, 대기 신청 없음
            assertThat(response.minPayout()).isEqualTo(50_000L);
            assertThat(response.hasPendingRequest()).isFalse();
        }

        @Test
        @DisplayName("성공 : 대기 중 신청이 있으면 신청 불가로 반환한다.")
        void getRevenueWithPendingRequest() {
            Creator creator = creatorWithId(1L);
            fixToday();
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(0L);
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(60_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));
            given(settlementRequestRepository.existsByCreatorIdAndStatus(
                1L, SettlementStatus.REQUESTED)).willReturn(true);

            DjRevenueResponse response = djRevenueService.getRevenue(creator);

            assertThat(response.hasPendingRequest()).isTrue();
            assertThat(response.canRequest()).isFalse();
        }

        @Test
        @DisplayName("성공 : 잔액이 없으면 0과 신청 불가를 반환한다.")
        void getRevenueWithoutBalance() {
            Creator creator = creatorWithId(1L);
            fixToday();
            given(listeningDailyRepository.sumSeconds(eq(1L), any(), any())).willReturn(0L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.empty());
            given(settlementRequestRepository.existsByCreatorIdAndStatus(
                1L, SettlementStatus.REQUESTED)).willReturn(false);

            DjRevenueResponse response = djRevenueService.getRevenue(creator);

            assertThat(response.thisMonthEstimate()).isEqualTo(0L);
            assertThat(response.balance()).isEqualTo(0L);
            assertThat(response.canRequest()).isFalse();
        }
    }

    @Nested
    @DisplayName("requestPayout 메서드 실행 시")
    class RequestPayout {

        @Test
        @DisplayName("성공 : 잔액이 최소 금액 이상이고 대기 신청이 없으면 신청을 저장한다.")
        void requestPayout() {
            Creator creator = creatorWithId(1L);
            given(clock.instant()).willReturn(Instant.parse("2026-06-18T00:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("UTC"));
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(60_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));
            given(settlementRequestRepository.existsByCreatorIdAndStatus(
                1L, SettlementStatus.REQUESTED)).willReturn(false);

            djRevenueService.requestPayout(creator);

            then(settlementRequestRepository).should().save(any(SettlementRequest.class));
        }

        @Test
        @DisplayName("실패 : 잔액이 최소 금액 미만이면 예외를 던진다.")
        void requestPayoutBelowMin() {
            Creator creator = creatorWithId(1L);
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(10_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));

            Exception exception = catchException(() -> djRevenueService.requestPayout(creator));

            assertThat(exception).isInstanceOf(BadRequestException.class);
            then(settlementRequestRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("실패 : 대기 중 신청이 있으면 예외를 던진다.")
        void requestPayoutDuplicate() {
            Creator creator = creatorWithId(1L);
            CreatorBalance balance = new CreatorBalance(1L);
            balance.apply(60_000L);
            given(creatorBalanceRepository.findById(1L)).willReturn(Optional.of(balance));
            given(settlementRequestRepository.existsByCreatorIdAndStatus(
                1L, SettlementStatus.REQUESTED)).willReturn(true);

            Exception exception = catchException(() -> djRevenueService.requestPayout(creator));

            assertThat(exception).isInstanceOf(BadRequestException.class);
            then(settlementRequestRepository).should(never()).save(any());
        }
    }
}

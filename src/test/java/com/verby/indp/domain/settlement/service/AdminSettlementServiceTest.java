package com.verby.indp.domain.settlement.service;

import static com.verby.indp.fixture.CreatorFixture.creatorWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.CreatorLedger;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.CreatorLedgerRepository;
import com.verby.indp.domain.settlement.repository.SettlementRequestRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AdminSettlementServiceTest {

    @InjectMocks
    private AdminSettlementService adminSettlementService;

    @Mock
    private SettlementRequestRepository settlementRequestRepository;

    @Mock
    private CreatorRepository creatorRepository;

    @Mock
    private CreatorBalanceRepository creatorBalanceRepository;

    @Mock
    private CreatorLedgerRepository creatorLedgerRepository;

    @Mock
    private Clock clock;

    private SettlementRequest requestWithId(long id, long creatorId, long amount) {
        SettlementRequest request = new SettlementRequest(
            creatorId, amount, LocalDateTime.of(2026, 6, 18, 10, 0));
        ReflectionTestUtils.setField(request, "settlementRequestId", id);
        return request;
    }

    private void fixNow() {
        given(clock.instant()).willReturn(Instant.parse("2026-06-19T00:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.of("UTC"));
    }

    @Nested
    @DisplayName("findSettlements 메서드 실행 시")
    class FindSettlements {

        @Test
        @DisplayName("성공 : 상태 미지정 시 전체를 DJ명과 함께 반환한다.")
        void findAll() {
            SettlementRequest request = requestWithId(1L, 10L, 60_000L);
            given(settlementRequestRepository.findAllByOrderByRequestedAtDesc())
                .willReturn(List.of(request));
            Creator creator = creatorWithId(10L);
            given(creatorRepository.findAllById(List.of(10L))).willReturn(List.of(creator));

            FindSettlementsResponse response = adminSettlementService.findSettlements(null);

            assertThat(response.settlements()).hasSize(1);
            assertThat(response.settlements().get(0).djName()).isEqualTo("DJ Parkwan");
            assertThat(response.settlements().get(0).amount()).isEqualTo(60_000L);
        }

        @Test
        @DisplayName("성공 : 상태 지정 시 해당 상태만 조회한다.")
        void findByStatus() {
            given(settlementRequestRepository.findAllByStatusOrderByRequestedAtDesc(
                SettlementStatus.REQUESTED)).willReturn(List.of());
            given(creatorRepository.findAllById(List.of())).willReturn(List.of());

            FindSettlementsResponse response =
                adminSettlementService.findSettlements(SettlementStatus.REQUESTED);

            assertThat(response.settlements()).isEmpty();
        }
    }

    @Nested
    @DisplayName("approve 메서드 실행 시")
    class Approve {

        @Test
        @DisplayName("성공 : 잔액을 차감하고 PAYOUT 원장을 기록하며 지급 처리한다.")
        void approve() {
            SettlementRequest request = requestWithId(1L, 10L, 60_000L);
            given(settlementRequestRepository.findById(1L)).willReturn(Optional.of(request));
            CreatorBalance balance = new CreatorBalance(10L);
            balance.apply(60_000L);
            given(creatorBalanceRepository.findById(10L)).willReturn(Optional.of(balance));
            fixNow();

            adminSettlementService.approve(1L);

            assertThat(balance.getBalance()).isZero();
            assertThat(request.getStatus()).isEqualTo(SettlementStatus.PAID);
            assertThat(request.getProcessedAt()).isNotNull();
            then(creatorLedgerRepository).should().save(any(CreatorLedger.class));
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 신청이면 예외를 던진다.")
        void approveNotFound() {
            given(settlementRequestRepository.findById(99L)).willReturn(Optional.empty());

            Exception exception = catchException(() -> adminSettlementService.approve(99L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("실패 : 잔액 정보가 없으면 예외를 던진다.")
        void approveNoBalance() {
            SettlementRequest request = requestWithId(1L, 10L, 60_000L);
            given(settlementRequestRepository.findById(1L)).willReturn(Optional.of(request));
            given(creatorBalanceRepository.findById(10L)).willReturn(Optional.empty());

            Exception exception = catchException(() -> adminSettlementService.approve(1L));

            assertThat(exception).isInstanceOf(NotFoundException.class);
            then(creatorLedgerRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("reject 메서드 실행 시")
    class Reject {

        @Test
        @DisplayName("성공 : 잔액 변동 없이 반려 처리한다.")
        void reject() {
            SettlementRequest request = requestWithId(1L, 10L, 60_000L);
            given(settlementRequestRepository.findById(1L)).willReturn(Optional.of(request));
            fixNow();

            adminSettlementService.reject(1L);

            assertThat(request.getStatus()).isEqualTo(SettlementStatus.REJECTED);
            assertThat(request.getProcessedAt()).isNotNull();
            then(creatorBalanceRepository).should(never()).findById(any());
        }
    }
}

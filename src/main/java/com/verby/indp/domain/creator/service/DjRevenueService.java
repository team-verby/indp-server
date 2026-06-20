package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.SettlementRequestRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjRevenueService {

    private final ListeningDailyRepository listeningDailyRepository;
    private final CreatorBalanceRepository creatorBalanceRepository;
    private final SettlementRequestRepository settlementRequestRepository;
    private final Clock clock;

    public DjRevenueResponse getRevenue(Creator creator) {
        Long creatorId = creator.getCreatorId();

        // 당월 예상 적립 — 실시간 청취시간 집계 / 단가
        YearMonth thisMonth = YearMonth.now(clock);
        long seconds = listeningDailyRepository.sumSeconds(
            creatorId, thisMonth.atDay(1), thisMonth.atEndOfMonth());
        long thisMonthEstimate = seconds / SettlementPolicy.SECONDS_PER_WON;

        // 확정 적립 잔액
        long balance = creatorBalanceRepository.findById(creatorId)
            .map(CreatorBalance::getBalance)
            .orElse(0L);

        long totalPaid = 0L; // 정산 지급(P3) 연동 시 PAID 합으로 대체
        boolean hasPendingRequest = settlementRequestRepository.existsByCreatorIdAndStatus(
            creatorId, SettlementStatus.REQUESTED);
        boolean canRequest = balance >= SettlementPolicy.MIN_PAYOUT_WON && !hasPendingRequest;

        return new DjRevenueResponse(
            thisMonthEstimate,
            balance,
            totalPaid,
            canRequest,
            SettlementPolicy.MIN_PAYOUT_WON,
            hasPendingRequest
        );
    }

    /**
     * 정산(출금)을 신청한다. 잔액이 최소 신청 금액 이상이고 대기 중 신청이 없을 때만 가능하다.
     * 신청 시점 잔액을 스냅샷으로 보관하며, 실제 차감은 어드민 지급 처리 시 이루어진다.
     */
    @Transactional
    public void requestPayout(Creator creator) {
        Long creatorId = creator.getCreatorId();

        long balance = creatorBalanceRepository.findById(creatorId)
            .map(CreatorBalance::getBalance)
            .orElse(0L);
        if (balance < SettlementPolicy.MIN_PAYOUT_WON) {
            throw new BadRequestException("최소 정산 금액에 도달하지 않았습니다.");
        }
        if (settlementRequestRepository.existsByCreatorIdAndStatus(
            creatorId, SettlementStatus.REQUESTED)) {
            throw new BadRequestException("이미 처리 대기 중인 정산 신청이 있습니다.");
        }

        settlementRequestRepository.save(
            new SettlementRequest(creatorId, balance, LocalDateTime.now(clock)));
    }
}

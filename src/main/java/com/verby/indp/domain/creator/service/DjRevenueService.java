package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.response.DjRevenueResponse;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjRevenueService {

    private static final int PAYOUT_DAY = 15;

    private final ListeningDailyRepository listeningDailyRepository;
    private final CreatorBalanceRepository creatorBalanceRepository;
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
        boolean canRequest = balance >= SettlementPolicy.MIN_PAYOUT_WON;
        LocalDate nextPayoutDate = LocalDate.now(clock).plusMonths(1).withDayOfMonth(PAYOUT_DAY);

        return new DjRevenueResponse(
            thisMonthEstimate,
            balance,
            totalPaid,
            canRequest,
            SettlementPolicy.MIN_PAYOUT_WON,
            nextPayoutDate
        );
    }
}

package com.verby.indp.domain.settlement.service;

import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.CreatorLedger;
import com.verby.indp.domain.settlement.LedgerType;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.CreatorLedgerRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 월 적립 (L1). 전월 청취시간을 집계해 단가(3,456초=1원)로 환산, 크리에이터 잔액에 가산한다.
 * 적립월(ref) 멱등성으로 중복 실행에 안전하다.
 */
@Service
@RequiredArgsConstructor
public class CreatorAccrualService {

    private final CreatorRepository creatorRepository;
    private final ListeningDailyRepository listeningDailyRepository;
    private final CreatorBalanceRepository creatorBalanceRepository;
    private final CreatorLedgerRepository creatorLedgerRepository;
    private final Clock clock;

    /** 스케줄러 진입점 — 직전 달(전월)분을 확정 적립한다. */
    @Transactional
    public void accruePreviousMonth() {
        accrueForMonth(YearMonth.now(clock).minusMonths(1));
    }

    @Transactional
    public void accrueForMonth(YearMonth month) {
        LocalDate from = month.atDay(1);
        LocalDate to = month.atEndOfMonth();
        String ref = month.toString(); // 예: '2026-06'

        List<Long> creatorIds = creatorRepository.findActiveCreatorIds();
        for (Long creatorId : creatorIds) {
            if (creatorLedgerRepository.existsByCreatorIdAndTypeAndRef(
                creatorId, LedgerType.ACCRUAL, ref)) {
                continue; // 이미 해당 월 적립됨 (멱등)
            }

            long seconds = listeningDailyRepository.sumSeconds(creatorId, from, to);
            long won = seconds / SettlementPolicy.SECONDS_PER_WON;
            if (won <= 0) {
                continue;
            }

            CreatorBalance balance = creatorBalanceRepository.findById(creatorId)
                .orElseGet(() -> creatorBalanceRepository.save(new CreatorBalance(creatorId)));
            long after = balance.apply(won);
            creatorLedgerRepository.save(
                new CreatorLedger(creatorId, LedgerType.ACCRUAL, won, after, ref));
        }
    }
}

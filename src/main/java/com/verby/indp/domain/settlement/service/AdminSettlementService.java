package com.verby.indp.domain.settlement.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.settlement.CreatorBalance;
import com.verby.indp.domain.settlement.CreatorLedger;
import com.verby.indp.domain.settlement.LedgerType;
import com.verby.indp.domain.settlement.SettlementRequest;
import com.verby.indp.domain.settlement.SettlementStatus;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse;
import com.verby.indp.domain.settlement.dto.response.FindSettlementsResponse.SettlementItem;
import com.verby.indp.domain.settlement.dto.response.SettlementTaxSecretResponse;
import com.verby.indp.domain.settlement.repository.CreatorBalanceRepository;
import com.verby.indp.domain.settlement.repository.CreatorLedgerRepository;
import com.verby.indp.domain.settlement.repository.SettlementRequestRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 어드민 정산 처리 (P3). 신청 목록 조회 및 지급/반려 처리.
 * 지급 시 잔액에서 신청 금액을 차감(PAYOUT 원장 기록)하고, 반려 시 잔액 변동은 없다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSettlementService {

    private final SettlementRequestRepository settlementRequestRepository;
    private final CreatorRepository creatorRepository;
    private final CreatorBalanceRepository creatorBalanceRepository;
    private final CreatorLedgerRepository creatorLedgerRepository;
    private final Clock clock;

    public FindSettlementsResponse findSettlements(SettlementStatus status) {
        List<SettlementRequest> requests = status == null
            ? settlementRequestRepository.findAllByOrderByRequestedAtDesc()
            : settlementRequestRepository.findAllByStatusOrderByRequestedAtDesc(status);

        Map<Long, Creator> creators = creatorRepository.findAllById(
                requests.stream().map(SettlementRequest::getCreatorId).distinct().toList())
            .stream()
            .collect(Collectors.toMap(Creator::getCreatorId, Function.identity()));

        List<SettlementItem> items = requests.stream()
            .map(request -> SettlementItem.of(request, creators.get(request.getCreatorId())))
            .toList();
        return new FindSettlementsResponse(items);
    }

    /**
     * 정산 처리를 위해 마스킹 없이 주민등록번호·계좌번호 전체 값을 복호화해 반환한다.
     * (목록에는 마스킹 값만 내려가므로, 실제 지급 시 이 API로 전체 값을 확인한다.)
     */
    public SettlementTaxSecretResponse getTaxSecret(long settlementRequestId) {
        SettlementRequest request = getRequest(settlementRequestId);
        return SettlementTaxSecretResponse.of(request.getTaxInfo());
    }

    /** 지급 처리 — 잔액에서 신청 금액을 차감하고 PAYOUT 원장을 기록한다. */
    @Transactional
    public void approve(long settlementRequestId) {
        SettlementRequest request = getRequest(settlementRequestId);

        CreatorBalance balance = creatorBalanceRepository.findById(request.getCreatorId())
            .orElseThrow(() -> new NotFoundException("크리에이터 잔액 정보가 없습니다."));
        long after = balance.apply(-request.getAmount());
        creatorLedgerRepository.save(new CreatorLedger(
            request.getCreatorId(), LedgerType.PAYOUT, -request.getAmount(), after,
            "payout:" + settlementRequestId));

        request.markPaid(LocalDateTime.now(clock));
    }

    /** 반려 처리 — 잔액 변동 없이 상태만 REJECTED로 전환한다. */
    @Transactional
    public void reject(long settlementRequestId) {
        SettlementRequest request = getRequest(settlementRequestId);
        request.markRejected(LocalDateTime.now(clock));
    }

    private SettlementRequest getRequest(long settlementRequestId) {
        return settlementRequestRepository.findById(settlementRequestId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 정산 신청입니다."));
    }
}

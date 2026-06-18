package com.verby.indp.domain.listening.service;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.listening.ListeningDaily;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.dto.request.HeartbeatRequest;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import com.verby.indp.domain.subscription.UserSubscriptionStatus;
import com.verby.indp.domain.subscription.repository.UserSubscriptionRepository;
import java.time.Clock;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 청취시간 기록 (L0). heartbeat 델타를 검증·클램프하여 일별 누적(listening_daily)에 반영한다.
 * 정책·산식은 노출하지 않으며, 비정상/비구독 청취는 조용히 무시한다.
 */
@Service
@RequiredArgsConstructor
public class ListeningService {

    private final ListeningDailyRepository listeningDailyRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final CreatorRepository creatorRepository;
    private final Clock clock;

    @Transactional
    public void heartbeat(User user, HeartbeatRequest request) {
        int delta = Math.min(request.seconds(), SettlementPolicy.MAX_BEAT_SEC);
        if (delta <= 0) {
            return;
        }

        LocalDate today = LocalDate.now(clock);

        // 비구독자·만료 구독은 적립 대상이 아니므로 무시 (어뷰징 1차 차단).
        boolean activeSubscriber = userSubscriptionRepository
            .existsByUserAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user, UserSubscriptionStatus.ACTIVE, today, today);
        if (!activeSubscriber) {
            return;
        }

        // 실재하는 활성 크리에이터에 대한 청취만 인정.
        if (!creatorRepository.existsByCreatorIdAndActiveTrue(request.creatorId())) {
            return;
        }

        listeningDailyRepository
            .findByUserIdAndCreatorIdAndYmd(user.getUserId(), request.creatorId(), today)
            .ifPresentOrElse(
                daily -> daily.addSeconds(delta, SettlementPolicy.DAILY_CAP_SEC),
                () -> listeningDailyRepository.save(new ListeningDaily(
                    user.getUserId(),
                    request.creatorId(),
                    today,
                    Math.min(delta, SettlementPolicy.DAILY_CAP_SEC)))
            );
    }
}

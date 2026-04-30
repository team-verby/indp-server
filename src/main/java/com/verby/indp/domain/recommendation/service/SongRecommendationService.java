package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.subscription.StoreSubscription;
import com.verby.indp.domain.subscription.SubscriptionStatus;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.PaymentType;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.playlist.service.PlaylistWebSocketService;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.domain.recommendation.dto.response.RegisterSongRecommendationResponse;
import com.verby.indp.domain.recommendation.repository.SongRecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.slack.SlackNotificationService;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongRecommendationService {

    private static final String RECOMMENDATION_FEE_KEY = "recommendation_fee";
    private static final String ORDER_NAME_PREFIX = "인디피_노래추천_";
    private static final Plan.PlanType RECOMMENDATION_ELIGIBLE_PLAN = Plan.PlanType.PLAN_B;

    private final SongRecommendationRepository songRecommendationRepository;
    private final StoreService storeService;
    private final PricePolicyService pricePolicyService;
    private final PlaylistService playlistService;
    private final PlaylistWebSocketService playlistWebSocketService;
    private final SlackNotificationService slackNotificationService;
    private final Clock clock;

    @Transactional
    public RegisterSongRecommendationResponse orderSongRecommendation(long storeId, String title,
        String artist, String vid, Integer playTime, String refereeName) {
        Store store = storeService.getStoreById(storeId);

        validateActiveSubscription(store);
        validateRecommendationEligiblePlan(store);
        validateBusinessOpen(store);
        validatePlaylistExists(store);

        Payment payment = buildPayment(store.getName());

        SongRecommendation songRecommendation = new SongRecommendation(store, title, artist, vid,
            playTime, refereeName, payment);
        songRecommendationRepository.save(songRecommendation);

        return RegisterSongRecommendationResponse.from(songRecommendation);
    }

    @Transactional
    public void confirmPayment(Payment payment) {
        SongRecommendation recommendation = getByPayment(payment);

        recommendation.updateStatus(SongRecommendation.RecommendationStatus.RECOMMENDED);
        PlaylistSong playlistSong = playlistService.addRecommendedSong(recommendation.getStore(),
            recommendation);

        playlistWebSocketService.sendSongRecommended(recommendation, playlistSong);
        slackNotificationService.handleMusicRecommendation(recommendation);
    }

    private Payment buildPayment(String storeName) {
        String orderName = createOrderName(storeName);
        int amount = pricePolicyService.getByPolicyKey(RECOMMENDATION_FEE_KEY).getAmount();
        return new Payment(PaymentType.SONG_RECOMMENDATION, orderName, amount);
    }

    private String createOrderName(String storeName) {
        return ORDER_NAME_PREFIX + storeName;
    }

    public FindStoreRecommendationsResponse findRecommendedSongs(long storeId) {
        Store store = storeService.getStoreById(storeId);
        return FindStoreRecommendationsResponse.from(
            songRecommendationRepository.findAllByStoreAndStatus(store,
                SongRecommendation.RecommendationStatus.RECOMMENDED)
        );
    }

    private SongRecommendation getByPayment(Payment payment) {
        return songRecommendationRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("추천 정보가 존재하지 않습니다."));
    }

    private void validateActiveSubscription(Store store) {
        boolean hasActiveSubscription = store.getSubscriptions().stream()
            .anyMatch(s -> s.getStatus() == SubscriptionStatus.ACTIVE);
        if (!hasActiveSubscription) {
            throw new BadRequestException("구독이 활성화된 매장이 아닙니다.");
        }
    }

    private void validateRecommendationEligiblePlan(Store store) {
        boolean isEligible = store.getSubscriptions().stream()
            .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE)
            .map(StoreSubscription::getPlan)
            .anyMatch(plan -> plan.getType() == RECOMMENDATION_ELIGIBLE_PLAN);
        if (!isEligible) {
            throw new BadRequestException("음악 추천이 가능한 플랜이 아닙니다.");
        }
    }

    private void validateBusinessOpen(Store store) {
        LocalDateTime now = LocalDateTime.now(clock);
        int currentDayOfWeek = now.getDayOfWeek().getValue();
        int prevDayOfWeek = currentDayOfWeek == 1 ? 7 : currentDayOfWeek - 1;
        LocalTime currentTime = now.toLocalTime();

        boolean isOpen = store.getBusinessHours().stream()
            .filter(bh -> !bh.isClosed())
            .anyMatch(bh -> isOpenAt(bh, currentDayOfWeek, prevDayOfWeek, currentTime));

        if (!isOpen) {
            throw new BadRequestException("현재 영업 중인 매장이 아닙니다.");
        }
    }

    private boolean isOpenAt(StoreBusinessHour bh, int dayOfWeek, int prevDayOfWeek, LocalTime time) {
        boolean isPastMidnight = bh.getCloseTime().isBefore(bh.getOpenTime());

        if (bh.getDayOfWeek() == dayOfWeek) {
            if (!isPastMidnight) {
                return !time.isBefore(bh.getOpenTime()) && !time.isAfter(bh.getCloseTime());
            }

            return !time.isBefore(bh.getOpenTime());
        }

        if (bh.getDayOfWeek() == prevDayOfWeek && isPastMidnight) {
            return !time.isAfter(bh.getCloseTime());
        }

        return false;
    }

    private void validatePlaylistExists(Store store) {
        if (store.getPlaylist() == null) {
            throw new BadRequestException("플레이리스트가 존재하지 않는 매장입니다.");
        }
    }
}

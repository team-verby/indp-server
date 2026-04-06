package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.playlist.service.PlaylistWebSocketService;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.domain.recommendation.dto.response.RegisterSongRecommendationResponse;
import com.verby.indp.domain.recommendation.repository.SongRecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongRecommendationService {

    private static final String RECOMMENDATION_FEE_KEY = "recommendation_fee";
    private static final String ORDER_NAME_PREFIX = "인디피_노래추천_";

    private final SongRecommendationRepository songRecommendationRepository;
    private final StoreService storeService;
    private final PricePolicyService pricePolicyService;
    private final PlaylistService playlistService;
    private final PlaylistWebSocketService playlistWebSocketService;

    @Transactional
    public RegisterSongRecommendationResponse recommend(long storeId, String title, String artist, String vid, Integer playTime, String refereeName) {
        Store store = storeService.getStoreById(storeId);
        // TODO: 음악 추천 전 검증 사항
        // 1. 구독이 활성화된 매장인지, planB를 구독한 매장인지
        // 2. 현재 시간 기준 영업 중인 매장인지
        // 3. 플레이리스트가 존재하는지
        Payment payment = buildPayment(store.getName());

        SongRecommendation songRecommendation = new SongRecommendation(store, title, artist, vid, playTime, refereeName, payment);
        songRecommendationRepository.save(songRecommendation);

        return RegisterSongRecommendationResponse.from(songRecommendation);
    }

    @Transactional
    public void confirmPayment(Payment payment) {
        SongRecommendation recommendation = getByPayment(payment);

        recommendation.updateStatus(SongRecommendation.RecommendationStatus.RECOMMENDED);
        PlaylistSong playlistSong = playlistService.addRecommendedSong(recommendation.getStore(), recommendation);

        playlistWebSocketService.sendSongRecommended(recommendation, playlistSong);
    }

    private Payment buildPayment(String storeName) {
        String orderName = createOrderName(storeName);
        int amount = pricePolicyService.getByPolicyKey(RECOMMENDATION_FEE_KEY).getAmount();
        return new Payment(orderName, amount);
    }

    private String createOrderName(String storeName) {
        return ORDER_NAME_PREFIX + storeName;
    }

    public FindStoreRecommendationsResponse findRecommendedSongs(long storeId) {
        Store store = storeService.getStoreById(storeId);
        return FindStoreRecommendationsResponse.from(
            songRecommendationRepository.findAllByStoreAndStatus(store, SongRecommendation.RecommendationStatus.RECOMMENDED)
        );
    }

    private SongRecommendation getByPayment(Payment payment) {
        return songRecommendationRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("추천 정보가 존재하지 않습니다."));
    }
}

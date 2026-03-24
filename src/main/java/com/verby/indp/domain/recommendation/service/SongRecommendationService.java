package com.verby.indp.domain.recommendation.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.config.PricePolicy;
import com.verby.indp.domain.config.PricePolicyRepository;
import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.payment.repository.PaymentRepository;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.recommendation.dto.response.RegisterSongRecommendationResponse;
import com.verby.indp.domain.recommendation.repository.SongRecommendationRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongRecommendationService {

    private static final String RECOMMENDATION_FEE_KEY = "recommendation_fee";
    private static final String RECOMMENDATION_ORDER_NAME = "노래 추천";

    private final SongRecommendationRepository songRecommendationRepository;
    private final StoreRepository storeRepository;
    private final PricePolicyRepository pricePolicyRepository;
    private final PaymentRepository paymentRepository;
    private final PlaylistService playlistService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public RegisterSongRecommendationResponse register(long storeId, String title, String artist, String vid, Integer playTime, String refereeName) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
        int fee = pricePolicyRepository.findById(RECOMMENDATION_FEE_KEY)
            .map(PricePolicy::getAmount)
            .orElseThrow(() -> new NotFoundException("추천 비용 설정이 존재하지 않습니다."));

        SongRecommendation recommendation = songRecommendationRepository.save(
            new SongRecommendation(store, title, artist, vid, playTime, refereeName, fee)
        );

        Payment payment = paymentRepository.save(new Payment(RECOMMENDATION_ORDER_NAME, fee));
        recommendation.setPayment(payment);

        return RegisterSongRecommendationResponse.from(recommendation);
    }

    @Transactional
    public void confirmPayment(Payment payment) {
        SongRecommendation recommendation = songRecommendationRepository.findByPayment(payment)
            .orElseThrow(() -> new NotFoundException("추천 정보가 존재하지 않습니다."));

        PlaylistSong playlistSong = playlistService.addRecommendedSong(recommendation.getStore(), recommendation);

        messagingTemplate.convertAndSend(
            "/topic/stores/" + recommendation.getStore().getStoreId(),
            Map.of(
                "type", "SONG_RECOMMENDED",
                "title", recommendation.getTitle(),
                "artist", recommendation.getArtist(),
                "vid", recommendation.getVid(),
                "playOrder", playlistSong.getPlayOrder(),
                "refereeName", recommendation.getRefereeName()
            )
        );
    }
}

package com.verby.indp.global.slack;

import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackNotificationService {

    private static final Logger slackLog = LoggerFactory.getLogger("com.verby.indp.slack");

    public void handlePlaylistRegenerationRequest(Store store) {
        slackLog.info(
            "[플레이리스트 재생성 요청]\n매장: {}",
            store.getName()
        );
    }

    public void handleMusicRecommendation(SongRecommendation recommendation) {
        slackLog.info(
            "[노래 추천]\n매장: {}\n가수: {}, 노래 제목: {}, 추천인 이름: {}",
            recommendation.getStore().getName(), recommendation.getArtist(),
            recommendation.getTitle(), recommendation.getRefereeName()
        );
    }

    public void handleApplyStoreStore(Store store) {
        slackLog.info(
            "[매장 도입 문의]\n매장: {}, 신청자 성함: {}, 신청자 연락처: {}\n계정: {} / {}",
            store.getName(), store.getOwner().getName(), store.getOwner().getPhone(),
            store.getOwner().getLoginId(), store.getOwner().getPassword()
        );
    }

}

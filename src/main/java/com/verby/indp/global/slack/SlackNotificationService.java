package com.verby.indp.global.slack;

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
        slackLog.info("플레이리스트 재생성 요청\n매장: {} (ID: {})", store.getName(), store.getStoreId());
    }

}

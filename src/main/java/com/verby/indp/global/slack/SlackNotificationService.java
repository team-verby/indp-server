package com.verby.indp.global.slack;

import com.verby.indp.domain.store.Store;
import com.verby.indp.global.infrastructure.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackNotificationService {

    private final ApiService apiService;

    @Value("${slack.webhook.uri}")
    private String webhookUri;

    @Async
    public void sendPlaylistRegenerateRequest(Store store) {
        String text = String.format("플레이리스트 재생성 요청\n매장: %s (ID: %d)", store.getName(), store.getStoreId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(Map.of("text", text), headers);
        apiService.post(entity, webhookUri, Void.class);
    }
}

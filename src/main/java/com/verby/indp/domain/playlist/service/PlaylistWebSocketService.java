package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final StoreRepository storeRepository;
    private final PlaylistService playlistService;

    public void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        Long storeId = extractStoreId(destination);
        if (storeId == null){
            return;
        }

        Long ownerId = (Long) accessor.getSessionAttributes().get("ownerId");
        boolean isOwner = validateOwner(ownerId, storeId);

        if (isOwner) {
            accessor.getSessionAttributes().put("storeId", storeId);
            log.debug("Owner {} subscribed to store {}", ownerId, storeId);
        }

        messagingTemplate.convertAndSendToUser(
            accessor.getSessionId(),
            "/queue/init",
            Map.of("type", "INIT", "isOwner", isOwner, "storeId", storeId),
            createHeaders(accessor.getSessionId())
        );
    }

    public void handleDisconnect(StompHeaderAccessor accessor) {
        Map<String, Object> attrs = accessor.getSessionAttributes();
        if (attrs == null) {
            return;
        }

        Long storeId = (Long) attrs.get("storeId");
        if (storeId == null) return;

        log.debug("Owner disconnected from store {}", storeId);
        storeRepository.findById(storeId).ifPresent(store -> {
            if (store.getPlaylist() != null) {
                playlistService.stopPlaylist(store.getPlaylist());
            }
        });
    }

    public void handleCurrentSong(long storeId, Long ownerId, Map<String, Object> payload) {
        if (!validateOwner(ownerId, storeId)) {
            log.warn("Unauthorized current-song update. ownerId={}, storeId={}", ownerId, storeId);
            return;
        }

        Number playlistSongId = (Number) payload.get("playlistSongId");
        if (playlistSongId == null) {
            return;
        }

        storeRepository.findById(storeId).ifPresent(store -> {
            if (store.getPlaylist() != null) {
                playlistService.updateCurrentSong(store.getPlaylist(), playlistSongId.longValue());
            }
        });
    }

    public void sendSongRecommended(SongRecommendation recommendation, PlaylistSong playlistSong) {
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

    private boolean validateOwner(Long ownerId, Long storeId) {
        if (ownerId == null){
            return false;
        }
        return storeRepository.findById(storeId)
            .map(store -> store.getOwner() != null
                && store.getOwner().getOwnerId().equals(ownerId))
            .orElse(false);
    }

    private Long extractStoreId(String destination) {
        if (destination == null || !destination.startsWith("/topic/stores/")){
            return null;
        }
        try {
            return Long.parseLong(destination.replace("/topic/stores/", "").split("/")[0]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }
}
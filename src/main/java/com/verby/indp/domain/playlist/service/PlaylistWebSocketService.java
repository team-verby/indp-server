package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
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
    private final StoreService storeService;

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
            Store store = storeService.getStoreById(storeId);
            Playlist playlist = store.getPlaylist();
            if (playlist != null) {
                playlist.connected();
            }
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

        Store store = storeService.getStoreById(storeId);
        Playlist playlist = store.getPlaylist();
        if (playlist != null) {
            playlist.disconnected();
        }
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
        Owner owner = storeService.getStoreById(storeId).getOwner();
        return owner.getOwnerId().equals(ownerId);
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
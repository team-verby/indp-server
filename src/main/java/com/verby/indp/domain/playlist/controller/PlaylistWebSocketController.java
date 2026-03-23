package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Controller
@RequiredArgsConstructor
public class PlaylistWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final StoreRepository storeRepository;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        if (destination == null || !destination.startsWith("/topic/stores/")) {
            return;
        }

        Long ownerId = (Long) accessor.getSessionAttributes().get("ownerId");
        if (ownerId == null) {
            return;
        }

        String storeIdStr = destination.replace("/topic/stores/", "").split("/")[0];
        long storeId;
        try {
            storeId = Long.parseLong(storeIdStr);
        } catch (NumberFormatException e) {
            return;
        }

        boolean isOwner = storeRepository.findById(storeId)
            .map(store -> store.getOwner() != null && store.getOwner().getOwnerId().equals(ownerId))
            .orElse(false);

        messagingTemplate.convertAndSend(
            "/topic/stores/" + storeId,
            Map.of("type", "INIT", "isOwner", isOwner, "storeId", storeId)
        );
    }

    @MessageMapping("/stores/{storeId}/current-song")
    public void receiveCurrentSong(
        @DestinationVariable long storeId,
        @Payload Map<String, Object> payload,
        SimpMessageHeaderAccessor headerAccessor
    ) {
        messagingTemplate.convertAndSend(
            "/topic/admin/stores/" + storeId + "/current-song",
            payload
        );
    }

    public void requestCurrentSong(long storeId) {
        messagingTemplate.convertAndSend(
            "/topic/stores/" + storeId,
            Map.of("type", "REQUEST_CURRENT_SONG", "storeId", storeId)
        );
    }
}

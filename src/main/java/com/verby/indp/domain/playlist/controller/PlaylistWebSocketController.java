package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.service.PlaylistWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlaylistWebSocketController {

    private final PlaylistWebSocketService playlistWebSocketService;

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        playlistWebSocketService.handleSubscribe(accessor);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        playlistWebSocketService.handleDisconnect(accessor);
    }

    @MessageMapping("/stores/{storeId}/current-song")
    public void receiveCurrentSong(
        @DestinationVariable long storeId,
        @Payload Map<String, Object> payload,
        SimpMessageHeaderAccessor headerAccessor
    ) {
        Long ownerId = (Long) headerAccessor.getSessionAttributes().get("ownerId");
        playlistWebSocketService.handleCurrentSong(storeId, ownerId, payload);
    }
}

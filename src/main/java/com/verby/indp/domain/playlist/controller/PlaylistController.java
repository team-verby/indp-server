package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.jwt.TokenManager;
import com.verby.indp.global.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaylistController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final PlaylistService playlistService;
    private final TokenManager tokenManager;
    private final StoreService storeService;
    private final SlackNotificationService slackNotificationService;

    @GetMapping("/stores/{storeId}/playlist")
    public ResponseEntity<FindStorePlaylistResponse> findStorePlaylist(
        @PathVariable long storeId,
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        Store store = storeService.getStoreById(storeId);
        boolean isOwner = resolveIsOwner(authorization, store);

        return ResponseEntity.ok(playlistService.getStorePlaylist(store, isOwner));
    }

    @PostMapping("/owner/stores/{storeId}/playlist/regenerate")
    public ResponseEntity<Void> regeneratePlaylist(@PathVariable long storeId) {
        Store store = storeService.getStoreById(storeId);
        slackNotificationService.sendPlaylistRegenerateRequest(store);
        return ResponseEntity.ok().build();
    }

    private boolean resolveIsOwner(String authorization, Store store) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return false;
        }
        try {
            Long ownerId = tokenManager.decodeOwnerToken(authorization.substring(BEARER_PREFIX.length()));
            return store.getOwner() != null && store.getOwner().getOwnerId().equals(ownerId);
        } catch (Exception e) {
            return false;
        }
    }
}

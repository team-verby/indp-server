package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.global.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaylistController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final PlaylistService playlistService;
    private final StoreRepository storeRepository;
    private final TokenManager tokenManager;

    @GetMapping("/stores/{storeId}/playlist")
    public ResponseEntity<FindStorePlaylistResponse> findStorePlaylist(
        @PathVariable long storeId,
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
        boolean isOwner = resolveIsOwner(authorization, store);
        return ResponseEntity.ok(playlistService.getStorePlaylist(store, isOwner));
    }

    @PostMapping("/owner/stores/{storeId}/playlist/regenerate")
    public ResponseEntity<Void> regeneratePlaylist(
        @RequestAttribute("ownerId") Long ownerId,
        @PathVariable long storeId
    ) {
        // TODO: slack 으로 플레이리스트 재생성 요청 알림
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

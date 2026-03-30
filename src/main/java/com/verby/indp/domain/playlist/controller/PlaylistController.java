package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.jwt.TokenManager;
import com.verby.indp.global.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/stores/{storeId}/playlist")
    public ResponseEntity<FindStorePlaylistResponse> findStorePlaylist(@PathVariable long storeId) {
        return ResponseEntity.ok(playlistService.getStorePlaylist(storeId));
    }
}

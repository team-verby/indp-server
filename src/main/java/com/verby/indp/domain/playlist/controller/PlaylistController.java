package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.service.PlaylistService;
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

    @GetMapping("/stores/{storeId}/playlists")
    public ResponseEntity<FindStorePlaylistResponse> findStorePlaylist(@PathVariable long storeId) {
        return ResponseEntity.ok(playlistService.getStorePlaylist(storeId));
    }
}

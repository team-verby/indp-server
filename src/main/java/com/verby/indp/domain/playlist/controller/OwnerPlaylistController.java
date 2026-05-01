package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import com.verby.indp.domain.playlist.service.OwnerPlaylistService;
import com.verby.indp.global.resolver.LoginOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerPlaylistController {

    private final OwnerPlaylistService playlistService;

    @GetMapping("/stores/{storeId}/playlists")
    public ResponseEntity<FindStorePlaylistByOwnerResponse> findStorePlaylist(
        @LoginOwner Owner owner, @PathVariable long storeId) {
        return ResponseEntity.ok(playlistService.getStorePlaylist(owner, storeId));
    }

    @PostMapping("/stores/{storeId}/playlists/regenerations")
    public ResponseEntity<Void> regeneratePlaylist(@LoginOwner Owner owner,
        @PathVariable long storeId) {
        playlistService.regeneratePlaylist(owner, storeId);
        return ResponseEntity.ok().build();
    }
}

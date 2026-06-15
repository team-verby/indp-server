package com.verby.indp.domain.creator.controller;

import com.verby.indp.domain.creator.dto.response.DjPlaylistDetailResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse;
import com.verby.indp.domain.creator.service.DjPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dj/playlists")
@RequiredArgsConstructor
public class DjPlaylistController {

    private final DjPlaylistService djPlaylistService;

    @GetMapping
    public ResponseEntity<FindDjPlaylistsResponse> getPlaylists() {
        return ResponseEntity.ok(djPlaylistService.getPlaylists());
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<DjPlaylistDetailResponse> getPlaylistDetail(@PathVariable long creatorId) {
        return ResponseEntity.ok(djPlaylistService.getPlaylistDetail(creatorId));
    }
}

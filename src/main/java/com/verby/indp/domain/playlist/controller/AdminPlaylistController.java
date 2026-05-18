package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.service.AdminPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminPlaylistController {

    private final AdminPlaylistService playlistService;

    @PostMapping("/scheduled-playlists")
    public ResponseEntity<Void> scheduledPlaylistUpdates(
        @RequestBody SchedulePlaylistsUpdateRequest request) {
        playlistService.addScheduledPlaylists(request);
        return ResponseEntity.noContent().build();
    }
}

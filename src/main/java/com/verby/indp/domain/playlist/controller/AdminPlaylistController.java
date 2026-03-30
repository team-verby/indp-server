package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/playlists")
@RequiredArgsConstructor
public class AdminPlaylistController {

    private final PlaylistService playlistService;
    private final StoreService storeService;

    @PostMapping("/schedule")
    public ResponseEntity<Void> schedulePlaylistUpdates(@RequestBody SchedulePlaylistsUpdateRequest request) {
        playlistService.addScheduledPlaylists(request);
        return ResponseEntity.noContent().build();
    }
}

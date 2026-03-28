package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistUpdateRequest;
import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/admin/playlists")
@RequiredArgsConstructor
public class AdminPlaylistController {

    private final PlaylistService playlistService;
    private final StoreService storeService;

    @PostMapping("/schedule")
    public ResponseEntity<Void> schedulePlaylistUpdates(
        @RequestBody List<SchedulePlaylistUpdateRequest> requests
    ) {
        for (SchedulePlaylistUpdateRequest request : requests) {
            Store store = storeService.getStoreById(request.storeId());
            List<ScheduledPlaylistSong> songs = IntStream.range(0, request.songs().size())
                .mapToObj(i -> {
                    SchedulePlaylistUpdateRequest.SongItem item = request.songs().get(i);
                    return new ScheduledPlaylistSong(item.title(), item.artist(), item.vid(), item.playTime(), i);
                })
                .toList();
            playlistService.saveScheduledUpdate(store, request.scheduledAt(), songs);
        }
        return ResponseEntity.noContent().build();
    }
}

package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.CreateFixedPlaylistSongRequest;
import com.verby.indp.domain.playlist.dto.response.FindFixedPlaylistSongsResponse;
import com.verby.indp.domain.playlist.service.AdminFixedPlaylistSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/fixed-songs")
@RequiredArgsConstructor
public class AdminFixedPlaylistSongController {

    private final AdminFixedPlaylistSongService fixedPlaylistSongService;

    @PostMapping
    public ResponseEntity<Void> addFixedPlaylistSong(
        @RequestBody CreateFixedPlaylistSongRequest request) {
        fixedPlaylistSongService.addFixedPlaylistSong(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<FindFixedPlaylistSongsResponse> findFixedPlaylistSongs() {
        return ResponseEntity.ok(fixedPlaylistSongService.findFixedPlaylistSongs());
    }

    @GetMapping("/active")
    public ResponseEntity<FindFixedPlaylistSongsResponse> findActiveFixedPlaylistSongs(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(fixedPlaylistSongService.findActiveFixedPlaylistSongs(date));
    }

    @DeleteMapping("/{fixedPlaylistSongId}")
    public ResponseEntity<Void> deleteFixedPlaylistSong(
        @PathVariable long fixedPlaylistSongId) {
        fixedPlaylistSongService.deleteFixedPlaylistSong(fixedPlaylistSongId);
        return ResponseEntity.noContent().build();
    }
}

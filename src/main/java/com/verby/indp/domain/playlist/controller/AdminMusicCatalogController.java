package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.domain.playlist.dto.response.SaveMusicCatalogResponse;
import com.verby.indp.domain.playlist.service.AdminMusicCatalogService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/music-catalog")
@RequiredArgsConstructor
public class AdminMusicCatalogController {

    private final AdminMusicCatalogService musicCatalogService;

    @GetMapping
    public ResponseEntity<FindMusicCatalogResponse> findMusicCatalog() {
        return ResponseEntity.ok(musicCatalogService.findMusicCatalog());
    }

    @PutMapping
    public ResponseEntity<SaveMusicCatalogResponse> updateMusicCatalog(
        @RequestBody UpdateMusicCatalogRequest request) {
        LocalDateTime savedAt = musicCatalogService.updateMusicCatalog(request);
        return ResponseEntity.ok(new SaveMusicCatalogResponse(savedAt));
    }
}

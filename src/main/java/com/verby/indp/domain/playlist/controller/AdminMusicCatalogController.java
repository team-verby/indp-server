package com.verby.indp.domain.playlist.controller;

import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.domain.playlist.service.AdminMusicCatalogService;
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
    public ResponseEntity<Void> updateMusicCatalog(
        @RequestBody UpdateMusicCatalogRequest request) {
        musicCatalogService.updateMusicCatalog(request);
        return ResponseEntity.noContent().build();
    }
}

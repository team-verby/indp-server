package com.verby.indp.domain.recommendation.controller;

import com.verby.indp.domain.recommendation.dto.response.FindStoreRecommendationsResponse;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminSongRecommendationController {

    private final SongRecommendationService songRecommendationService;

    @GetMapping("/admin/stores/{storeId}/songs/recommendations")
    public ResponseEntity<FindStoreRecommendationsResponse> findRecommendedSongs(@PathVariable long storeId) {
        return ResponseEntity.ok(songRecommendationService.findRecommendedSongs(storeId));
    }

}

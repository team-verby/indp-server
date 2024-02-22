package com.verby.indp.domain.recommendation.controller;

import com.verby.indp.domain.recommendation.dto.request.RegisterRecommendationRequest;
import com.verby.indp.domain.recommendation.service.RecommendationService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/music/recommendations")
    public ResponseEntity<Void> registerRecommendation(@RequestBody RegisterRecommendationRequest request) {
        long recommendationId = recommendationService.registerRecommendation(request);
        URI uri = URI.create("/api/music/recommendations/" + recommendationId);

        return ResponseEntity.created(uri).build();
    }

}

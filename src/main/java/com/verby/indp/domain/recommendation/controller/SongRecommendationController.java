package com.verby.indp.domain.recommendation.controller;

import com.verby.indp.domain.policy.PricePolicyService;
import com.verby.indp.domain.recommendation.dto.request.RegisterSongRecommendationRequest;
import com.verby.indp.domain.recommendation.dto.response.RegisterSongRecommendationResponse;
import com.verby.indp.domain.recommendation.service.SongRecommendationService;
import com.verby.indp.domain.store.dto.response.FindRecommendationFeeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SongRecommendationController {

    private final SongRecommendationService songRecommendationService;
    private final PricePolicyService pricePolicyService;

    @PostMapping("/stores/{storeId}/songs/recommendations")
    public ResponseEntity<RegisterSongRecommendationResponse> registerRecommendation(
        @PathVariable long storeId,
        @RequestBody RegisterSongRecommendationRequest request
    ) {
        RegisterSongRecommendationResponse response = songRecommendationService.orderSongRecommendation(
            storeId, request.title(), request.artist(), request.vid(), request.playTime(), request.refereeName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/recommendation-fee")
    public ResponseEntity<FindRecommendationFeeResponse> findRecommendationFee() {
        return ResponseEntity.ok(FindRecommendationFeeResponse.from(pricePolicyService.getByPolicyKey("recommendation_fee")));
    }
}

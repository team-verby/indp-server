package com.verby.indp.domain.recommendation.dto.request;

public record RegisterSongRecommendationRequest(
    String artist,
    String title,
    String vid,
    Integer playTime,
    String refereeName
) {

}

package com.verby.indp.domain.recommendation.event;

import com.verby.indp.domain.notification.dto.RecommendationMail;

public record RecommendationMailEvent(
   RecommendationMail request
) {
}

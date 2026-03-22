package com.verby.indp.domain.recommendation.repository;

import com.verby.indp.domain.recommendation.SongRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRecommendationRepository extends JpaRepository<SongRecommendation, Long> {
}

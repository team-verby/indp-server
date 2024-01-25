package com.verby.indp.domain.recommendation.repository;

import com.verby.indp.domain.recommendation.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

}

package com.verby.indp.domain.recommendation.repository;

import com.verby.indp.domain.payment.Payment;
import com.verby.indp.domain.recommendation.SongRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRecommendationRepository extends JpaRepository<SongRecommendation, Long> {

    Optional<SongRecommendation> findByPayment(Payment payment);
}

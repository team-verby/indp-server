package com.verby.indp.domain.creator.repository;

import com.verby.indp.domain.creator.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    boolean existsByEmail(String email);

    Optional<Creator> findByEmail(String email);
}

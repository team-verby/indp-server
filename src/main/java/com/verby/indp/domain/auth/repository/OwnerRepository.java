package com.verby.indp.domain.auth.repository;

import com.verby.indp.domain.auth.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}

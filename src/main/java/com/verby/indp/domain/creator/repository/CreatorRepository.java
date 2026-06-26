package com.verby.indp.domain.creator.repository;

import com.verby.indp.domain.creator.Creator;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    boolean existsByEmail(String email);

    boolean existsByCreatorIdAndActiveTrue(Long creatorId);

    Optional<Creator> findByEmail(String email);

    /** 스케줄 자동 라이브 대상(시드 DJ) 목록. */
    List<Creator> findAllByAutoLiveTrueAndActiveTrue();

    /** 활성 크리에이터 ID 목록 (월 적립 집계 대상). */
    @Query("SELECT c.creatorId FROM Creator c WHERE c.active = true")
    List<Long> findActiveCreatorIds();
}

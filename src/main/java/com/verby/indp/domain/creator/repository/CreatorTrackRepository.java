package com.verby.indp.domain.creator.repository;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreatorTrackRepository extends JpaRepository<CreatorTrack, Long> {

    List<CreatorTrack> findAllByCreatorOrderByCreatedAtAsc(Creator creator);

    int countByCreator(Creator creator);

    @Query("SELECT COALESCE(SUM(t.secs), 0) FROM CreatorTrack t WHERE t.creator = :creator")
    int sumSecsByCreator(@Param("creator") Creator creator);
}

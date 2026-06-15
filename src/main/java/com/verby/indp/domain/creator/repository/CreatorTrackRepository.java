package com.verby.indp.domain.creator.repository;

import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreatorTrackRepository extends JpaRepository<CreatorTrack, Long> {

    List<CreatorTrack> findAllByCreatorOrderByCreatedAtAsc(Creator creator);

    int countByCreator(Creator creator);
}

package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.ScheduledPlaylistUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledPlaylistUpdateRepository extends JpaRepository<ScheduledPlaylistUpdate, Long> {

    List<ScheduledPlaylistUpdate> findAllByStatusAndScheduledAtLessThanEqual(
        ScheduledPlaylistUpdate.UpdateStatus status,
        LocalDateTime scheduledAt
    );
}

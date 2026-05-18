package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.ScheduledPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledPlaylistUpdateRepository extends JpaRepository<ScheduledPlaylist, Long> {

    List<ScheduledPlaylist> findAllByStatusAndScheduledAtLessThanEqual(
        ScheduledPlaylist.UpdateStatus status,
        LocalDateTime scheduledAt
    );
}

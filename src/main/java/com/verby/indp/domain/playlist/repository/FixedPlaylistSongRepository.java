package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.FixedPlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FixedPlaylistSongRepository extends JpaRepository<FixedPlaylistSong, Long> {

    List<FixedPlaylistSong> findAllByOrderByStartDateAscTargetHourAscPositionAsc();

    List<FixedPlaylistSong> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(
        LocalDate startDate,
        LocalDate endDate
    );
}

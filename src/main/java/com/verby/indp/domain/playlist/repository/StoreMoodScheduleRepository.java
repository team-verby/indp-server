package com.verby.indp.domain.playlist.repository;

import com.verby.indp.domain.playlist.StoreMoodSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMoodScheduleRepository extends JpaRepository<StoreMoodSchedule, Long> {

    List<StoreMoodSchedule> findAllByOrderByStoreNameAscHourAsc();
}

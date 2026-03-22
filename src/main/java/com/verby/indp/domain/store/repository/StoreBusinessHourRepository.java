package com.verby.indp.domain.store.repository;

import com.verby.indp.domain.store.StoreBusinessHour;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreBusinessHourRepository extends JpaRepository<StoreBusinessHour, Long> {

    @Query("SELECT h FROM StoreBusinessHour h JOIN FETCH h.store s JOIN FETCH s.playlist WHERE h.dayOfWeek = :dayOfWeek AND h.closeTime = :closeTime AND h.isClosed = false")
    List<StoreBusinessHour> findByDayOfWeekAndCloseTime(@Param("dayOfWeek") int dayOfWeek, @Param("closeTime") LocalTime closeTime);
}

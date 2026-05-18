package com.verby.indp.domain.store.repository;

import com.verby.indp.domain.store.StoreBusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface StoreBusinessHourRepository extends JpaRepository<StoreBusinessHour, Long> {

    @Query("SELECT h FROM StoreBusinessHour h JOIN FETCH h.store s JOIN FETCH s.playlist WHERE h.dayOfWeek = :dayOfWeek AND h.closeTime > :fromCloseTime AND h.closeTime <= :closeTime AND h.isClosed = false")
    List<StoreBusinessHour> findByDayOfWeekAndCloseTimeBetween(@Param("dayOfWeek") int dayOfWeek,
        @Param("fromCloseTime") LocalTime fromCloseTime, @Param("closeTime") LocalTime closeTime);
}

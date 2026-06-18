package com.verby.indp.domain.playlist.dto.response;

import com.verby.indp.domain.playlist.StoreMoodSchedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record FindMoodScheduleResponse(
    LocalDateTime savedAt,
    List<StoreSchedule> schedules
) {

    public static FindMoodScheduleResponse from(LocalDateTime savedAt,
        List<StoreMoodSchedule> rows) {
        Map<String, List<Slot>> grouped = new LinkedHashMap<>();
        for (StoreMoodSchedule row : rows) {
            grouped.computeIfAbsent(row.getStoreName(), key -> new ArrayList<>())
                .add(new Slot(row.getHour(), row.getMood()));
        }
        List<StoreSchedule> schedules = grouped.entrySet().stream()
            .map(entry -> new StoreSchedule(entry.getKey(), entry.getValue()))
            .toList();
        return new FindMoodScheduleResponse(savedAt, schedules);
    }

    public record StoreSchedule(
        String storeName,
        List<Slot> slots
    ) {

    }

    public record Slot(
        int hour,
        String mood
    ) {

    }
}

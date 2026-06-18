package com.verby.indp.domain.playlist.dto.request;

import java.util.List;

public record UpdateMoodScheduleRequest(
    List<StoreSchedule> schedules
) {

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

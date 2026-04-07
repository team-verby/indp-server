package com.verby.indp.domain.store.dto.request;

import java.time.LocalTime;

public record TimePreference(
    LocalTime startTime,
    LocalTime endTime,
    String mood
) {

}

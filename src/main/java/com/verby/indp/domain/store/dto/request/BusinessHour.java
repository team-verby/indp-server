package com.verby.indp.domain.store.dto.request;

import java.time.LocalTime;

public record BusinessHour(
    int dayOfWeek,
    LocalTime openTime,
    LocalTime closeTime,
    boolean isClosed
) {
}

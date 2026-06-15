package com.verby.indp.domain.creator.dto.response;

import java.time.LocalDate;

public record UserSubscriptionResponse(
    String planName,
    int monthlyRate,
    int usagePeriod,
    LocalDate startDate,
    LocalDate endDate
) {

}

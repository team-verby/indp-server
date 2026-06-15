package com.verby.indp.domain.creator.dto.response;

import java.time.LocalDate;

public record DjRevenueResponse(
    Long thisMonthEstimate,
    Long totalPaid,
    LocalDate nextPayoutDate
) {

}

package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;

import java.time.LocalTime;
import java.util.List;

public record FindStoreSummaryResponse(
    String name,
    String industry,
    String address,
    List<BusinessHourItem> businessHours,
    String subscriptionStatus,
    String planType
) {

    public record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isClosed) {
        static BusinessHourItem from(StoreBusinessHour bh) {
            return new BusinessHourItem(bh.getDayOfWeek(), bh.getOpenTime(), bh.getCloseTime(), bh.isClosed());
        }
    }

    public static FindStoreSummaryResponse from(Store store) {
        var latestSub = store.findLatestPaidSubscription();
        return new FindStoreSummaryResponse(
            store.getName(),
            store.getIndustry(),
            store.getAddress(),
            store.getBusinessHours().stream().map(BusinessHourItem::from).toList(),
            latestSub.map(s -> s.getStatus().name()).orElse(null),
            latestSub.map(s -> s.getPlan().getType()).orElse(null)
        );
    }
}

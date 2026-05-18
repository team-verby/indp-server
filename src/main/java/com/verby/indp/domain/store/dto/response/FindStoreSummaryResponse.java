package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.StorePhoto;

import com.verby.indp.domain.subscription.StoreSubscription;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public record FindStoreSummaryResponse(
    String name,
    String industry,
    String address,
    List<BusinessHourItem> businessHours,
    String subscriptionStatus,
    String planType,
    String mainPhotoUrl
) {

    public record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime,
                                   boolean isClosed) {

        static BusinessHourItem from(StoreBusinessHour bh) {
            return new BusinessHourItem(bh.getDayOfWeek(), bh.getOpenTime(), bh.getCloseTime(),
                bh.isClosed());
        }
    }

    public static FindStoreSummaryResponse from(Store store) {
        Optional<StoreSubscription> subscription = store.findLatestActiveOrPendingSubscription();
        return new FindStoreSummaryResponse(
            store.getName(),
            store.getIndustry(),
            store.getAddress(),
            store.getBusinessHours().stream().map(BusinessHourItem::from).toList(),
            subscription.map(s -> s.getStatus().name()).orElse(null),
            subscription.map(s -> s.getPlan().getType().name()).orElse(null),
            store.getPhotos().stream().filter(StorePhoto::isMain).findFirst().map(StorePhoto::getImageUrl).orElse(null)
        );
    }
}

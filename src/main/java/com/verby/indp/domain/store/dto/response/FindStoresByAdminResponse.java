package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.StorePhoto;

import java.time.LocalTime;
import java.util.List;

public record FindStoresByAdminResponse(List<StoreItem> stores) {

    public static FindStoresByAdminResponse from(List<Store> stores) {
        return new FindStoresByAdminResponse(
            stores.stream().map(StoreItem::from).toList()
        );
    }

    private record StoreItem(
        Long storeId,
        String name,
        String industry,
        String address,
        String mainPhotoUrl,
        List<BusinessHourItem> businessHours
    ) {
        private static StoreItem from(Store store) {
            String mainPhotoUrl = store.getPhotos().stream()
                .filter(StorePhoto::isMain)
                .findFirst()
                .map(StorePhoto::getImageUrl)
                .orElse(null);

            List<BusinessHourItem> businessHours = store.getBusinessHours().stream()
                .map(BusinessHourItem::from)
                .toList();

            return new StoreItem(store.getStoreId(), store.getName(), store.getIndustry(),
                store.getAddress(), mainPhotoUrl, businessHours);
        }
    }

    private record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isClosed) {
        public static BusinessHourItem from(StoreBusinessHour h) {
            return new BusinessHourItem(h.getDayOfWeek(), h.getOpenTime(), h.getCloseTime(), h.isClosed());
        }
    }
}

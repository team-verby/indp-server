package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.StorePhoto;
import java.time.LocalTime;
import java.util.List;

public record FindStoreDetailByAdminResponse(
    Long storeId,
    String name,
    String industry,
    String address,
    String playedMusic,
    List<BusinessHourItem> businessHours,
    List<PhotoItem> photos
) {

    public static FindStoreDetailByAdminResponse from(Store store) {
        List<BusinessHourItem> hours = store.getBusinessHours().stream()
            .map(BusinessHourItem::from)
            .toList();

        List<PhotoItem> photos = store.getPhotos().stream()
            .map(PhotoItem::from)
            .toList();

        String playedMusic = store.getStoreMusic() != null ? store.getStoreMusic().getPlayedMusic() : null;

        return new FindStoreDetailByAdminResponse(
            store.getStoreId(), store.getName(), store.getIndustry(),
            store.getAddress(), playedMusic, hours, photos
        );
    }

    private record BusinessHourItem(int dayOfWeek, LocalTime openTime, LocalTime closeTime, boolean isClosed) {
        public static BusinessHourItem from(StoreBusinessHour h) {
            return new BusinessHourItem(h.getDayOfWeek(), h.getOpenTime(), h.getCloseTime(), h.isClosed());
        }
    }

    private record PhotoItem(String imageUrl, boolean isMain) {
        public static PhotoItem from(StorePhoto p) {
            return new PhotoItem(p.getImageUrl(), p.isMain());
        }
    }
}

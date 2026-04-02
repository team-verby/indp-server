package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.playlist.service.CurrentSongResolver;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.subscription.StoreSubscription;

import java.time.LocalDate;
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
        SubscriptionItem subscription,
        CurrentSongItem currentSong
    ) {
        private static StoreItem from(Store store) {
            SubscriptionItem subscription = store.getRecentSubscription()
                    .map(SubscriptionItem::from)
                    .orElse(null);
            CurrentSongItem currentSong = CurrentSongResolver.resolveCurrentSong(store)
                .map(CurrentSongItem::from)
                .orElse(null);

            return new StoreItem(store.getStoreId(), store.getName(), subscription, currentSong);
        }
    }

    private record SubscriptionItem(String plan, LocalDate startDate, LocalDate endDate, String status) {
        private static SubscriptionItem from(StoreSubscription subscription) {
            return new SubscriptionItem(subscription.getPlan().getType(), subscription.getStartDate(), subscription.getEndDate(),
                    subscription.getStatus().name());
        }
    }

    private record CurrentSongItem(long playlistSongId, String title, String artist, String vid, int elapsedSeconds) {
        private static CurrentSongItem from(CurrentSong song) {
            return new CurrentSongItem(song.playlistSongId(), song.title(), song.artist(), song.vid(), song.elapsedSeconds());
        }
    }
}

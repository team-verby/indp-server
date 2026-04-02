package com.verby.indp.domain.store.dto.response;

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
        PlayingSong playingSong
    ) {
        private static StoreItem from(Store store) {
            SubscriptionItem subscription = store.getRecentSubscription()
                    .map(SubscriptionItem::from)
                    .orElse(null);
            CurrentSongResolver.resolveCurrentSong(store);
            PlayingSong playingSong = store.getPlaylist().isPlaying() ? PlayingSong.from()
            return new StoreItem(store.getStoreId(), store.getName(), subscription);
        }
    }

    private record SubscriptionItem(String plan, LocalDate startDate, LocalDate endDate, String status) {
        private static SubscriptionItem from(StoreSubscription subscription) {
            return new SubscriptionItem(subscription.getPlan().getType(), subscription.getStartDate(), subscription.getEndDate(),
                    subscription.getStatus().name());
        }
    }

    private record PlayingSong(String title, String artist) {
        private static PlayingSong from(CurrentSong song) {
            return new PlayingSong(song.getTitle(), song.getArtist());
        }
    }
}

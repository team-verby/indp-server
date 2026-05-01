package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.subscription.StoreSubscription;

import java.time.LocalDate;
import java.util.List;

public record FindStoresByAdminResponse(
    List<StoreItem> stores, int totalPages, long totalElements
) {

    public record StoreItem(
        Long storeId,
        String name,
        List<SubscriptionItem> subscriptions,
        CurrentSongItem currentSong
    ) {
        public static StoreItem from(Store store, CurrentSong currentSong) {
            List<SubscriptionItem> subscriptions = store.getSubscriptions().stream()
                .map(SubscriptionItem::from)
                .toList();
            CurrentSongItem currentSongItem =
                currentSong == null ? null : CurrentSongItem.from(currentSong);

            return new StoreItem(store.getStoreId(), store.getName(), subscriptions, currentSongItem);
        }
    }

    private record SubscriptionItem(String plan, LocalDate startDate, LocalDate endDate,
                                    String status) {

        private static SubscriptionItem from(StoreSubscription subscription) {
            return new SubscriptionItem(subscription.getPlan().getType().name(),
                subscription.getStartDate(), subscription.getEndDate(),
                subscription.getStatus().name());
        }
    }

    private record CurrentSongItem(long playlistSongId, String title, String artist, String vid,
                                   int elapsedSeconds) {

        private static CurrentSongItem from(CurrentSong song) {
            return new CurrentSongItem(song.playlistSongId(), song.title(), song.artist(),
                song.vid(), song.elapsedSeconds());
        }
    }
}

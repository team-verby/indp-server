package com.verby.indp.fixture;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.plan.Plan;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreApply;
import com.verby.indp.domain.store.dto.request.BusinessHour;
import com.verby.indp.domain.store.vo.Vibe;
import com.verby.indp.domain.subscription.StoreSubscription;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.test.util.ReflectionTestUtils;

public class StoreFixture {

    public static Store storeWithPlaylist(Playlist playlist) {
        Store store = storeWithOwner(OwnerFixture.owner());
        ReflectionTestUtils.setField(store, "playlist", playlist);
        return store;
    }

    public static Store store() {
        return storeWithOwner(OwnerFixture.owner());
    }

    public static Store storeWithOwner(Owner owner) {
        Store store = createStore(owner, allDaysOpen());
        StoreSubscription subscription = StoreSubscriptionFixture.activeSubscription();
        store.addSubscription(subscription);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    public static Store inactiveStore() {
        Store store = createStore(OwnerFixture.owner(), allDaysOpen());
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    public static Store inactiveStoreWithOwner(Owner owner) {
        Store store = createStore(owner, allDaysOpen());
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    public static Store closedStore() {
        Store store = createStore(OwnerFixture.owner(), allDaysClosed());
        StoreSubscription subscription = StoreSubscriptionFixture.activeSubscription();
        store.addSubscription(subscription);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    public static Store storeWithActiveSubscriptionAndPlan(Plan plan) {
        Store store = createStore(OwnerFixture.owner(), allDaysOpen());
        StoreSubscription subscription = StoreSubscriptionFixture.activeSubscriptionWithPlan(plan);
        store.addSubscription(subscription);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    public static Store closedStoreWithActiveSubscriptionAndPlan(Plan plan) {
        Store store = createStore(OwnerFixture.owner(), allDaysClosed());
        StoreSubscription subscription = StoreSubscriptionFixture.activeSubscriptionWithPlan(plan);
        store.addSubscription(subscription);
        ReflectionTestUtils.setField(store, "storeId", 1L);
        return store;
    }

    private static Store createStore(Owner owner, List<BusinessHour> businessHours) {
        StoreApply storeApply = new StoreApply("홍길동", "010-1234-5678");
        return new Store(
            storeApply,
            owner,
            "카페 공명 홍대점",
            "카페",
            "서울 마포구 와우산로17길 11-8",
            "20대 중반 ~ 30대 초반",
            3,
            StoreMusicFixture.storeMusic(),
            List.of(Vibe.CALM, Vibe.NATURAL),
            businessHours,
            List.of("https://example.com/photo.jpg")
        );
    }

    private static List<BusinessHour> allDaysOpen() {
        return IntStream.rangeClosed(1, 7)
            .mapToObj(day -> new BusinessHour(day, LocalTime.of(8, 0), LocalTime.of(23, 0), false))
            .toList();
    }

    private static List<BusinessHour> allDaysClosed() {
        return IntStream.rangeClosed(1, 7)
            .mapToObj(day -> new BusinessHour(day, null, null, true))
            .toList();
    }
}

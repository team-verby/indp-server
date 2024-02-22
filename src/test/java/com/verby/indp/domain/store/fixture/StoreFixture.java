package com.verby.indp.domain.store.fixture;

import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.theme.Theme;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StoreFixture {

    private static final String STORE_NAME = "StoreName";
    private static final String STORE_ADDRESS = "StoreAddress";
    private static final List<String> IMAGE_URL_LIST = List.of("imageUrl1");
    public static final Region STORE_REGION = Region.SEOUL;

    public static Store store() {
        return new Store(
            STORE_NAME,
            STORE_ADDRESS,
            STORE_REGION,
            IMAGE_URL_LIST,
            List.of(),
            List.of()
        );
    }

    public static Store store(
        List<Theme> themes,
        List<SongForm> songForms
    ) {
        return new Store(
            STORE_NAME,
            STORE_ADDRESS,
            STORE_REGION,
            IMAGE_URL_LIST,
            themes,
            songForms
        );
    }

    public static Store store(
        List<Theme> themes,
        List<SongForm> songForms,
        Region region
    ) {
        return new Store(
            STORE_NAME,
            STORE_ADDRESS,
            region,
            IMAGE_URL_LIST,
            themes,
            songForms
        );
    }

    public static List<Store> stores(
        List<Theme> themes,
        List<SongForm> songForms,
        int count
    ) {
        return IntStream.range(0, count)
            .mapToObj(i -> store(themes, songForms))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Store> stores(
        List<Theme> themes,
        List<SongForm> songForms,
        int count,
        Region region
    ) {
        return IntStream.range(0, count)
            .mapToObj(i -> store(themes, songForms, region))
            .collect(Collectors.toCollection(ArrayList::new));
    }

}

package com.verby.indp.domain.store.fixture;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.constant.Region;
import java.util.List;
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
            IMAGE_URL_LIST
        );
    }

    public static List<Store> stores(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> store())
            .toList();
    }

}

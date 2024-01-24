package com.verby.indp.acceptance;


import static com.verby.indp.domain.store.fixture.StoreFixture.stores;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.acceptance.support.StoreApiSupporter;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("store 인수 테스트")
class StoreAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("메인 페이지에서 매장 목록을 조회한다.")
    void findSimpleStores() {
        // given
        int count = 20;
        int page = 0;
        int size = 10;

        List<Store> stores = stores(count);
        storeRepository.saveAll(stores);

        Pageable pageable = PageRequest.of(page, size);
        stores.sort((o1, o2) -> (int) (o1.getStoreId() - o2.getStoreId()));
        Page<Store> pageStores = new PageImpl<>(stores.subList(page, size), pageable, count);

        FindSimpleStoresResponse expected = FindSimpleStoresResponse.from(pageStores);

        // when
        ExtractableResponse<Response> result = StoreApiSupporter.findSimpleStores(page, size);

        // then
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.as(FindSimpleStoresResponse.class)).isEqualTo(expected);

    }

}

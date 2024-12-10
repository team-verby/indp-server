package com.verby.indp.acceptance;


import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.song.fixture.SongFormFixture.songForm;
import static com.verby.indp.domain.store.fixture.StoreFixture.stores;
import static com.verby.indp.domain.theme.fixture.ThemeFixture.theme;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.acceptance.support.StoreApiSupporter;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.song.repository.SongFormRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.theme.Theme;
import com.verby.indp.domain.theme.repository.ThemeRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
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

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private SongFormRepository songFormRepository;

    @Test
    @DisplayName("메인 페이지에서 매장 목록을 조회한다.")
    void findSimpleStores() {
        // given
        int count = 20;
        int page = 0;
        int size = 10;

        Region 서울 = region("서울");
        regionRepository.save(서울);
        List<Store> stores = stores(서울, List.of(), List.of(), count);
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

    @Test
    @DisplayName("특정 지역의 매장 목록을 조회한다.")
    void findStoresOfRegion() {
        // given
        Region 서울  = region("서울");
        Region 경기  = region("경기");
        regionRepository.save(서울);
        regionRepository.save(경기);

        int seoulCount = 5;
        int gyeonggiCount = 15;

        int page = 0;
        int size = 10;

        Theme theme = theme();
        themeRepository.save(theme);

        SongForm songForm = songForm();
        songFormRepository.save(songForm);

        List<Store> seoulStores = stores(List.of(theme), List.of(songForm), seoulCount, 서울);
        List<Store> gyeonggiStores = stores(List.of(theme), List.of(songForm), gyeonggiCount, 경기);
        List<Store> allStores = new ArrayList<>();
        allStores.addAll(seoulStores);
        allStores.addAll(gyeonggiStores);

        storeRepository.saveAll(allStores);

        Pageable pageable = PageRequest.of(page, size);

        gyeonggiStores.sort((o1, o2) -> (int) (o1.getStoreId() - o2.getStoreId()));
        Page<Store> pageStores = new PageImpl<>(gyeonggiStores.subList(page, size), pageable,
            gyeonggiCount);

        FindStoresResponse expected = FindStoresResponse.from(pageStores);

        // when
        ExtractableResponse<Response> result = StoreApiSupporter.findStores(page, size, 경기.getRegion());

        // then
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.as(FindStoresResponse.class)).isEqualTo(expected);

    }

    @Test
    @DisplayName("전체 지역의 매장 목록을 조회한다.")
    void findStores() {
        // given
        Region 서울  = region("서울");
        Region 경기  = region("경기");
        regionRepository.save(서울);
        regionRepository.save(경기);

        int seoulCount = 5;
        int gyeonggiCount = 15;

        int page = 0;
        int size = 10;

        Theme theme = theme();
        themeRepository.save(theme);

        SongForm songForm = songForm();
        songFormRepository.save(songForm);

        List<Store> seoulStores = stores(List.of(theme), List.of(songForm), seoulCount, 서울);
        List<Store> gyeonggiStores = stores(List.of(theme), List.of(songForm), gyeonggiCount, 경기);
        List<Store> allStores = new ArrayList<>();
        allStores.addAll(seoulStores);
        allStores.addAll(gyeonggiStores);

        storeRepository.saveAll(allStores);

        Pageable pageable = PageRequest.of(page, size);

        allStores.sort((o1, o2) -> (int) (o1.getStoreId() - o2.getStoreId()));
        Page<Store> pageStores = new PageImpl<>(allStores.subList(page, size), pageable,
            seoulCount + gyeonggiCount);

        FindStoresResponse expected = FindStoresResponse.from(pageStores);

        // when
        ExtractableResponse<Response> result = StoreApiSupporter.findStores(page, size);

        // then
        assertThat(result.statusCode()).isEqualTo(200);
        assertThat(result.as(FindStoresResponse.class)).isEqualTo(expected);

    }

}

package com.verby.indp.domain.store.repository;

import static com.verby.indp.domain.region.fixture.RegionFixture.region;
import static com.verby.indp.domain.song.fixture.SongFormFixture.songForm;
import static com.verby.indp.domain.store.fixture.StoreFixture.stores;
import static com.verby.indp.domain.theme.fixture.ThemeFixture.theme;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.song.repository.SongFormRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.theme.Theme;
import com.verby.indp.domain.theme.repository.ThemeRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private SongFormRepository songFormRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Nested
    @DisplayName("findAllByOrderByStoreIdAsc 메소드 실행 시")
    class FindAllByOrderByStoreIdAsc {

        @Test
        @DisplayName("성공: id 오름차순으로 size 만큼 페이징 조회를 한다.")
        void findAllByOrderByCreatedAtAsc() {
            // given
            int count = 20;
            int page = 0;
            int size = 10;

            Theme theme = theme();
            themeRepository.save(theme);

            SongForm songForm = songForm();
            songFormRepository.save(songForm);

            Region 서울 = region("서울");
            regionRepository.save(서울);

            Pageable pageable = PageRequest.of(page, size);
            List<Store> stores = stores(서울, List.of(theme), List.of(songForm), count);

            storeRepository.saveAll(stores);

            stores.sort((o1, o2) -> (int) (o1.getStoreId() - o2.getStoreId()));
            List<Store> expected = stores.subList(0, size);

            // when
            Page<Store> result = storeRepository.findAllByOrderByStoreIdAsc(
                pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(count);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.getContent()).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("findAllByRegionOrderByStoreIdAsc 메소드 실행 시")
    class FindAllByRegionOrderByStoreIdAsc {

        @Test
        @DisplayName("성공: 특정 지역의 매장을 id 오름차순으로 size 만큼 페이징 조회를 한다.")
        void findAllByRegionOrderByStoreIdAsc() {
            // given
            int seoulCount = 5;
            int gyeonggiCount = 15;

            int page = 0;
            int size = 10;

            Region 서울 = region("서울");
            Region 경기 = region("경기");
            regionRepository.save(서울);
            regionRepository.save(경기);

            Theme theme = theme();
            themeRepository.save(theme);

            SongForm songForm = songForm();
            songFormRepository.save(songForm);

            Pageable pageable = PageRequest.of(page, size);
            List<Store> seoulStores = stores(List.of(theme), List.of(songForm), seoulCount, 서울);
            List<Store> gyeonggiStores = stores(List.of(theme), List.of(songForm), gyeonggiCount,
                경기);

            storeRepository.saveAll(gyeonggiStores);
            storeRepository.saveAll(seoulStores);

            seoulStores.sort((o1, o2) -> (int) (o1.getStoreId() - o2.getStoreId()));
            List<Store> expected = seoulStores.subList(0, Math.min(size, seoulCount));

            // when
            Page<Store> result = storeRepository.findAllByRegionOrderByStoreIdAsc(pageable, 서울);

            // then
            assertThat(result.getTotalElements()).isEqualTo(seoulCount);
            assertThat(result.hasNext()).isFalse();
            assertThat(result.getContent()).isEqualTo(expected);
        }

    }

}

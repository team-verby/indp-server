package com.verby.indp.domain.store.repository;

import static com.verby.indp.domain.store.fixture.StoreFixture.stores;
import static org.assertj.core.api.Assertions.assertThat;

import com.verby.indp.domain.store.Store;
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

            Pageable pageable = PageRequest.of(page, size);
            List<Store> stores = stores(count);

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

}

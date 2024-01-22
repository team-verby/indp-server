package com.verby.indp.domain.store.service;

import static com.verby.indp.domain.store.fixture.StoreFixture.stores;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Nested
    @DisplayName("findSimpleStores 메소드 실행 시")
    class FindSimpleStores {

        @Test
        @DisplayName("성공: size 만큼 간단한 매장 정보를 조회 한다.")
        void findSimpleStores() {
            // given
            int count = 20;
            int page = 0;
            int size = 10;

            List<Store> stores = stores(count);
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> pageStores = new PageImpl<>(stores.subList(page, size), pageable, count);

            FindSimpleStoresResponse expected = FindSimpleStoresResponse.from(pageStores);

            when(storeRepository.findAllByOrderByCreatedAtAsc(pageable)).thenReturn(pageStores);

            // when
            FindSimpleStoresResponse result = storeService.findSimpleStores(pageable);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.pageInfo().hasNext()).isTrue();
            assertThat(result.pageInfo().totalElements()).isEqualTo(count);
            assertThat(result.stores()).hasSize(size);

        }
    }

}

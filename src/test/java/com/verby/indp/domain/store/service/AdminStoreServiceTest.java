package com.verby.indp.domain.store.service;

import static com.verby.indp.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.service.CurrentSongResolver;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class AdminStoreServiceTest {

    @InjectMocks
    private AdminStoreService adminStoreService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CurrentSongResolver currentSongResolver;

    @Nested
    @DisplayName("findStores 메서드 실행 시")
    class FindStores {

        @Test
        @DisplayName("성공 : 매장 목록을 반환한다.")
        void findStores() {
            // given
            Page<Store> page = new PageImpl<>(List.of());
            given(storeRepository.findAllByOrderByStoreIdAsc(any())).willReturn(page);

            // when
            FindStoresByAdminResponse result = adminStoreService.findStores(
                PageRequest.of(0, 10));

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("findStore 메서드 실행 시")
    class FindStore {

        @Test
        @DisplayName("성공 : 매장 상세를 반환한다.")
        void findStore() {
            // given
            Store mockStore = store();
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            // when
            FindStoreByAdminResponse result = adminStoreService.findStore(1L);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void findStoreWithNotExist() {
            // given
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> adminStoreService.findStore(999L));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }
}

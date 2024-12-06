package com.verby.indp.domain.store.service;


import static com.verby.indp.domain.song.fixture.SongFormFixture.songForm;
import static com.verby.indp.domain.store.constant.Region.경기;
import static com.verby.indp.domain.store.constant.Region.서울;
import static com.verby.indp.domain.store.fixture.StoreFixture.store;
import static com.verby.indp.domain.store.fixture.StoreFixture.storesWithId;
import static com.verby.indp.domain.theme.fixture.ThemeFixture.theme;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.song.repository.SongFormRepository;
import com.verby.indp.domain.song.vo.SongFormName;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.constant.Region;
import com.verby.indp.domain.store.dto.request.AddStoreByAdminRequest;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.theme.Theme;
import com.verby.indp.domain.theme.repository.ThemeRepository;
import com.verby.indp.domain.theme.vo.ThemeName;
import java.util.ArrayList;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private SongFormRepository songFormRepository;

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

            List<Store> stores = storesWithId(List.of(), List.of(), count);
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> pageStores = new PageImpl<>(stores.subList(page, size), pageable, count);

            FindSimpleStoresResponse expected = FindSimpleStoresResponse.from(pageStores);

            when(storeRepository.findAllByOrderByStoreIdAsc(pageable)).thenReturn(pageStores);

            // when
            FindSimpleStoresResponse result = storeService.findSimpleStores(pageable);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.pageInfo().hasNext()).isTrue();
            assertThat(result.pageInfo().totalElements()).isEqualTo(count);
            assertThat(result.stores()).hasSize(size);

        }
    }

    @Nested
    @DisplayName("findStores 메소드 실행 시")
    class FindStores {

        @Test
        @DisplayName("성공: size 만큼 특정 지역의 매장 정보를 조회 한다.")
        void findStoresOfRegion() {
            // given
            Region region = 서울;
            int seoulCount = 5;

            int page = 0;
            int size = 10;

            List<Store> seoulStores = storesWithId(List.of(), List.of(), seoulCount, 서울);
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> pageStores = new PageImpl<>(
                seoulStores.subList(page, Math.min(size, seoulCount)), pageable, seoulCount);

            FindStoresResponse expected = FindStoresResponse.from(pageStores);

            when(storeRepository.findAllByRegionOrderByStoreIdAsc(pageable, region)).thenReturn(
                pageStores);

            // when
            FindStoresResponse result = storeService.findStores(pageable, region);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.pageInfo().hasNext()).isFalse();
            assertThat(result.pageInfo().totalElements()).isEqualTo(seoulCount);
            assertThat(result.stores()).hasSize(Math.min(size, seoulCount));

        }

        @Test
        @DisplayName("성공: size 만큼 전체 지역의 매장 정보를 조회 한다.")
        void findStores() {
            // given
            Region nullRegion = null;
            int seoulCount = 5;
            int gyeonggiCount = 15;

            int page = 0;
            int size = 10;

            List<Store> seoulStores = storesWithId(List.of(), List.of(), seoulCount, 서울);
            List<Store> gyeonggiStores = storesWithId(List.of(), List.of(), gyeonggiCount, 경기);
            List<Store> allStores = new ArrayList<>();
            allStores.addAll(seoulStores);
            allStores.addAll(gyeonggiStores);

            Pageable pageable = PageRequest.of(page, size);
            Page<Store> pageStores = new PageImpl<>(
                allStores.subList(page, size), pageable, seoulCount + gyeonggiCount);

            FindStoresResponse expected = FindStoresResponse.from(pageStores);

            when(storeRepository.findAllByOrderByStoreIdAsc(pageable)).thenReturn(
                pageStores);

            // when
            FindStoresResponse result = storeService.findStores(pageable, nullRegion);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.pageInfo().hasNext()).isTrue();
            assertThat(result.pageInfo().totalElements()).isEqualTo(seoulCount + gyeonggiCount);
            assertThat(result.stores()).hasSize(size);

        }
    }

    @Nested
    @DisplayName("findStoresByAdmin 메소드 실행 시")
    class FindStoresByAdmin {

        @Test
        @DisplayName("성공: size 만큼 매장 정보를 조회 한다.")
        void findStoresOfRegion() {
            // given
            int page = 0;
            int size = 10;
            int count = 1;

            List<Store> stores = storesWithId(List.of(theme()), List.of(songForm()), count, 서울);
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> pageStores = new PageImpl<>(stores, pageable, count);

            FindStoresByAdminResponse expected = FindStoresByAdminResponse.from(pageStores);

            when(storeRepository.findAllByOrderByStoreIdAsc(pageable)).thenReturn(
                pageStores);

            // when
            FindStoresByAdminResponse result = storeService.findStoresByAdmin(pageable);

            // then
            assertThat(result).isEqualTo(expected);
            assertThat(result.pageInfo().hasNext()).isFalse();
            assertThat(result.pageInfo().totalElements()).isEqualTo(count);
            assertThat(result.stores()).hasSize(count);
        }

    }

    @Nested
    @DisplayName("findStoreByAdmin 메소드 실행 시")
    class FindStoreByAdmin {

        @Test
        @DisplayName("성공 : storeId 에 해당하는 매장 정보를 조회한다.")
        void findStoreById() {
            // given
            long storeId = 1;
            Store store = store();

            FindStoreByAdminResponse expected = FindStoreByAdminResponse.from(store);

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

            // when
            FindStoreByAdminResponse result = storeService.findStoreByAdmin(storeId);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("예외(NotFoundException) : storeId 에 해당하는 매장이 없을 경우 예외가 발생한다.")
        void exceptionWhenStoreNotFound() {
            // given
            long storeId = 1;

            when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

            // when
            Exception exception = catchException(() -> storeService.findStoreByAdmin(storeId));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteStoreByAdmin 메소드 실행 시")
    class DeleteStoreByAdmin {

        @Test
        @DisplayName("성공 : storeId 에 해당하는 매장 정보를 삭제한다.")
        void deleteStoreById() {
            // given
            long storeId = 1;

            // when
            storeService.deleteStoreByAdmin(storeId);

            // then
            verify(storeRepository, times(1)).deleteById(storeId);
        }
    }

    @Nested
    @DisplayName("addStore 메소드 실행 시")
    class AddStores {

        @Test
        @DisplayName("성공 : 새로운 매장 정보가 저징된다.")
        void saveStore() {
            // given
            Store store = store();
            ReflectionTestUtils.setField(store, "storeId", 1L);
            AddStoreByAdminRequest request = new AddStoreByAdminRequest(
                store.getName(), store.getAddress(), store.getRegion(),
                store.getImage().get(0), store.getThemes(), store.getSongForms());

            when(storeRepository.save(any(Store.class))).thenReturn(store);

            // when
            long result = storeService.addStore(request);

            // then
            assertThat(result).isEqualTo(store.getStoreId());
        }

        @Test
        @DisplayName("성공 : 새로운 테마와 매장 정보가 저징된다.")
        void saveStoreWithNewTheme() {
            // given
            ThemeName newThemeName = new ThemeName("newThemeName");
            Theme newTheme = new Theme(newThemeName.getName());
            Store store = store(List.of(newTheme), List.of());
            ReflectionTestUtils.setField(store, "storeId", 1L);

            AddStoreByAdminRequest request = new AddStoreByAdminRequest(
                store.getName(), store.getAddress(), store.getRegion(),
                store.getImage().get(0), store.getThemes(), store.getSongForms());

            when(themeRepository.findByName(any(ThemeName.class))).thenReturn(Optional.empty());
            when(storeRepository.save(any(Store.class))).thenReturn(store);

            // when
            long result = storeService.addStore(request);

            // then
            assertThat(result).isEqualTo(store.getStoreId());
            verify(themeRepository, times(1)).save(newTheme);
        }

        @Test
        @DisplayName("성공 : 새로운 곡 구성과 매장 정보가 저징된다.")
        void saveStoreWithSongForm() {
            // given
            SongFormName newSongFormName = new SongFormName("newSongFormName");
            SongForm newSongForm = new SongForm(newSongFormName.getName());
            Store store = store(List.of(), List.of(newSongForm));
            ReflectionTestUtils.setField(store, "storeId", 1L);

            AddStoreByAdminRequest request = new AddStoreByAdminRequest(
                store.getName(), store.getAddress(), store.getRegion(),
                store.getImage().get(0), store.getThemes(), store.getSongForms());

            when(songFormRepository.findByName(any(SongFormName.class))).thenReturn(Optional.empty());
            when(storeRepository.save(any(Store.class))).thenReturn(store);

            // when
            long result = storeService.addStore(request);

            // then
            assertThat(result).isEqualTo(store.getStoreId());
            verify(songFormRepository, times(1)).save(newSongForm);
        }
    }

}

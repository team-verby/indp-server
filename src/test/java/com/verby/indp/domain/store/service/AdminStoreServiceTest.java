package com.verby.indp.domain.store.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.service.CurrentSongResolver;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.MusicGenre.PreferenceType;
import com.verby.indp.domain.store.dto.request.TimePreference;
import com.verby.indp.domain.store.dto.request.UpdateGenresByAdminRequest;
import com.verby.indp.domain.store.dto.request.UpdateGenresByAdminRequest.GenrePreferenceItem;
import com.verby.indp.domain.store.dto.request.UpdateTimePreferencesByAdminRequest;
import com.verby.indp.domain.store.vo.Genre;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.verby.indp.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.BDDMockito.given;

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
            given(storeRepository.findAll(PageRequest.of(0, 10))).willReturn(page);

            // when
            FindStoresByAdminResponse result = adminStoreService.findStores(
                PageRequest.of(0, 10));

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("updateGenres 메서드 실행 시")
    class UpdateGenres {

        @Test
        @DisplayName("성공 : 기존 장르를 새 목록으로 교체한다.")
        void updateGenres() {
            // given
            Store mockStore = store();
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateGenresByAdminRequest request = new UpdateGenresByAdminRequest(
                List.of(
                    new GenrePreferenceItem(Genre.BALLAD, PreferenceType.LIKE),
                    new GenrePreferenceItem(Genre.ROCK, PreferenceType.DISLIKE)
                )
            );

            // when
            adminStoreService.updateGenres(1L, request);

            // then
            assertThat(mockStore.getStoreMusic().getGenres()).hasSize(2);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void updateGenresWithNotExistStore() {
            // given
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            UpdateGenresByAdminRequest request = new UpdateGenresByAdminRequest(
                List.of(new GenrePreferenceItem(Genre.BALLAD, PreferenceType.LIKE))
            );

            // when
            Exception exception = catchException(
                () -> adminStoreService.updateGenres(999L, request));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("updateTimePreferences 메서드 실행 시")
    class UpdateTimePreferences {

        @Test
        @DisplayName("성공 : 기존 시간대별 무드를 새 목록으로 교체한다.")
        void updateTimePreferences() {
            // given
            Store mockStore = store();
            given(storeRepository.findById(1L)).willReturn(Optional.of(mockStore));

            UpdateTimePreferencesByAdminRequest request = new UpdateTimePreferencesByAdminRequest(
                List.of(
                    new TimePreference(LocalTime.of(10, 0), LocalTime.of(14, 0), "낮 무드"),
                    new TimePreference(LocalTime.of(14, 0), LocalTime.of(20, 0), "저녁 무드")
                )
            );

            // when
            adminStoreService.updateTimePreferences(1L, request);

            // then
            assertThat(mockStore.getStoreMusic().getMusicTimePreferences()).hasSize(2);
        }

        @Test
        @DisplayName("실패 : 존재하지 않는 매장이면 예외를 던진다.")
        void updateTimePreferencesWithNotExistStore() {
            // given
            given(storeRepository.findById(999L)).willReturn(Optional.empty());

            UpdateTimePreferencesByAdminRequest request = new UpdateTimePreferencesByAdminRequest(
                List.of(new TimePreference(LocalTime.of(10, 0), LocalTime.of(14, 0), "낮 무드"))
            );

            // when
            Exception exception = catchException(
                () -> adminStoreService.updateTimePreferences(999L, request));

            // then
            assertThat(exception).isInstanceOf(NotFoundException.class);
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

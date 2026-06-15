package com.verby.indp.domain.playlist.service;

import static com.verby.indp.fixture.FixedPlaylistSongFixture.fixedPlaylistSong;
import static com.verby.indp.fixture.StoreFixture.store;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.verby.indp.domain.playlist.FixedPlaylistSong;
import com.verby.indp.domain.playlist.dto.request.CreateFixedPlaylistSongRequest;
import com.verby.indp.domain.playlist.dto.response.FindFixedPlaylistSongsResponse;
import com.verby.indp.domain.playlist.repository.FixedPlaylistSongRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminFixedPlaylistSongServiceTest {

    @InjectMocks
    private AdminFixedPlaylistSongService adminFixedPlaylistSongService;

    @Mock
    private StoreService storeService;

    @Mock
    private FixedPlaylistSongRepository fixedPlaylistSongRepository;

    @Nested
    @DisplayName("addFixedPlaylistSong 메서드 실행 시")
    class AddFixedPlaylistSong {

        @Test
        @DisplayName("성공 : 특정곡을 저장한다.")
        void addFixedPlaylistSong() {
            // given
            Store store = store();
            given(storeService.getStoreByName("카페 공명")).willReturn(store);
            given(fixedPlaylistSongRepository.save(any(FixedPlaylistSong.class)))
                .willAnswer(inv -> inv.getArgument(0));

            CreateFixedPlaylistSongRequest request = new CreateFixedPlaylistSongRequest(
                "카페 공명", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30),
                14, 3, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259);

            // when
            Exception exception = catchException(
                () -> adminFixedPlaylistSongService.addFixedPlaylistSong(request));

            // then
            assertThat(exception).isNull();
            then(fixedPlaylistSongRepository).should().save(any(FixedPlaylistSong.class));
        }

        @Test
        @DisplayName("성공 : 엑셀 모드(시간대 없음) 특정곡을 저장한다.")
        void addFixedPlaylistSongWithoutHour() {
            // given
            Store store = store();
            given(storeService.getStoreByName("카페 공명")).willReturn(store);
            given(fixedPlaylistSongRepository.save(any(FixedPlaylistSong.class)))
                .willAnswer(inv -> inv.getArgument(0));

            CreateFixedPlaylistSongRequest request = new CreateFixedPlaylistSongRequest(
                "카페 공명", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30),
                null, 25, "안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259);

            // when
            Exception exception = catchException(
                () -> adminFixedPlaylistSongService.addFixedPlaylistSong(request));

            // then
            assertThat(exception).isNull();
            then(fixedPlaylistSongRepository).should().save(any(FixedPlaylistSong.class));
        }
    }

    @Nested
    @DisplayName("findFixedPlaylistSongs 메서드 실행 시")
    class FindFixedPlaylistSongs {

        @Test
        @DisplayName("성공 : 저장된 특정곡 전체 목록을 반환한다.")
        void findFixedPlaylistSongs() {
            // given
            given(fixedPlaylistSongRepository
                .findAllByOrderByStartDateAscTargetHourAscPositionAsc())
                .willReturn(List.of(fixedPlaylistSong()));

            // when
            FindFixedPlaylistSongsResponse response =
                adminFixedPlaylistSongService.findFixedPlaylistSongs();

            // then
            assertThat(response.fixedSongs()).hasSize(1);
            then(fixedPlaylistSongRepository).should()
                .findAllByOrderByStartDateAscTargetHourAscPositionAsc();
        }
    }

    @Nested
    @DisplayName("findActiveFixedPlaylistSongs 메서드 실행 시")
    class FindActiveFixedPlaylistSongs {

        @Test
        @DisplayName("성공 : 해당 날짜에 적용되는 특정곡 목록을 반환한다.")
        void findActiveFixedPlaylistSongs() {
            // given
            LocalDate date = LocalDate.of(2026, 6, 15);
            given(fixedPlaylistSongRepository
                .findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(eq(date), eq(date)))
                .willReturn(List.of(fixedPlaylistSong()));

            // when
            FindFixedPlaylistSongsResponse response =
                adminFixedPlaylistSongService.findActiveFixedPlaylistSongs(date);

            // then
            assertThat(response.fixedSongs()).hasSize(1);
            assertThat(response.fixedSongs().get(0).title()).isEqualTo("안녕 나의 사랑");
            then(fixedPlaylistSongRepository).should()
                .findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
        }
    }

    @Nested
    @DisplayName("deleteFixedPlaylistSong 메서드 실행 시")
    class DeleteFixedPlaylistSong {

        @Test
        @DisplayName("성공 : 특정곡을 삭제한다.")
        void deleteFixedPlaylistSong() {
            // when
            adminFixedPlaylistSongService.deleteFixedPlaylistSong(1L);

            // then
            then(fixedPlaylistSongRepository).should().deleteById(1L);
        }
    }
}

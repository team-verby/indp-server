package com.verby.indp.domain.playlist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest.SchedulePlaylistItem;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest.SchedulePlaylistItem.SongItem;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import static com.verby.indp.fixture.StoreFixture.store;

import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminPlaylistServiceTest {

    @InjectMocks
    private AdminPlaylistService adminPlaylistService;

    @Mock
    private StoreService storeService;

    @Mock
    private ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Nested
    @DisplayName("addScheduledPlaylists 메서드 실행 시")
    class AddScheduledPlaylists {

        @Test
        @DisplayName("성공 : 스케줄 플레이리스트를 저장한다.")
        void addScheduledPlaylists() {
            // given
            Store store = store();
            given(storeService.getStoreByName("카페 공명")).willReturn(store);
            given(scheduledPlaylistUpdateRepository.save(any(ScheduledPlaylist.class)))
                .willAnswer(inv -> inv.getArgument(0));

            SongItem song = new SongItem("안녕 나의 사랑", "성시경", "5zAEiu3SaO4", 259);
            SchedulePlaylistItem item = new SchedulePlaylistItem("카페 공명", List.of(song),
                LocalDateTime.now().plusHours(1));
            SchedulePlaylistsUpdateRequest request = new SchedulePlaylistsUpdateRequest(
                List.of(item));

            // when
            Exception exception = catchException(
                () -> adminPlaylistService.addScheduledPlaylists(request));

            // then
            assertThat(exception).isNull();
            then(scheduledPlaylistUpdateRepository).should().save(any(ScheduledPlaylist.class));
        }
    }
}

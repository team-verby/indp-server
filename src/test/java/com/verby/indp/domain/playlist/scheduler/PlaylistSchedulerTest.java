package com.verby.indp.domain.playlist.scheduler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.playlist.service.PlaylistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaylistSchedulerTest {

    @InjectMocks
    private PlaylistScheduler playlistScheduler;

    @Mock
    private PlaylistService playlistService;

    @Test
    @DisplayName("applyScheduledPlaylistUpdates : 스케줄된 플레이리스트 업데이트를 적용한다.")
    void applyScheduledPlaylistUpdates() {
        // given
        willDoNothing().given(playlistService).applyDueScheduledUpdates();

        // when
        playlistScheduler.applyScheduledPlaylistUpdates();

        // then
        then(playlistService).should().applyDueScheduledUpdates();
    }

    @Test
    @DisplayName("deleteRecommendedSongsAtClose : 마감 매장의 추천 곡 삭제를 요청한다.")
    void deleteRecommendedSongsAtClose() {
        // given
        willDoNothing().given(playlistService).deleteRecommendedSongsOfClosingStores(anyInt(), any(), any());

        // when
        playlistScheduler.deleteRecommendedSongsAtClose();

        // then
        then(playlistService).should().deleteRecommendedSongsOfClosingStores(anyInt(), any(), any());
    }
}

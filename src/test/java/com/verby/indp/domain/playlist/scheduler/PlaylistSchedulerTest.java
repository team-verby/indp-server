package com.verby.indp.domain.playlist.scheduler;

import static com.verby.indp.fixture.StoreFixture.store;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.repository.StoreBusinessHourRepository;
import java.time.LocalTime;
import java.util.List;
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
    private StoreBusinessHourRepository storeBusinessHourRepository;

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
    @DisplayName("deleteRecommendedSongsAtClose : 마감 매장의 추천 곡을 삭제한다.")
    void deleteRecommendedSongsAtClose() {
        // given
        given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(
            org.mockito.ArgumentMatchers.anyInt(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        )).willReturn(List.of());

        // when
        playlistScheduler.deleteRecommendedSongsAtClose();

        // then
        then(playlistService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("deleteRecommendedSongsAtClose : 마감 매장이 있으면 추천 곡을 삭제한다.")
    void deleteRecommendedSongsAtCloseWithClosingStore() {
        // given
        Store store = store();
        StoreBusinessHour businessHour = new StoreBusinessHour(store, 1, LocalTime.of(10, 0), LocalTime.of(22, 0), false);

        given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(
            org.mockito.ArgumentMatchers.anyInt(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        )).willReturn(List.of(businessHour));

        willDoNothing().given(playlistService).deleteRecommendedSongs(store);

        // when
        playlistScheduler.deleteRecommendedSongsAtClose();

        // then
        then(playlistService).should().deleteRecommendedSongs(store);
    }
}

package com.verby.indp.domain.playlist.scheduler;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.verby.indp.domain.playlist.service.PlaylistService;
import com.verby.indp.domain.store.StoreBusinessHour;
import com.verby.indp.domain.store.repository.StoreBusinessHourRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        willDoNothing().given(playlistService).applyDueScheduledUpdates();

        playlistScheduler.applyScheduledPlaylistUpdates();

        then(playlistService).should().applyDueScheduledUpdates();
    }

    @Test
    @DisplayName("deleteRecommendedSongsAtClose : 마감 매장의 추천 곡을 삭제한다.")
    void deleteRecommendedSongsAtClose() {
        given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(
            org.mockito.ArgumentMatchers.anyInt(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        )).willReturn(List.of());

        playlistScheduler.deleteRecommendedSongsAtClose();

        then(playlistService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("deleteRecommendedSongsAtClose : 마감 매장이 있으면 추천 곡을 삭제한다.")
    void deleteRecommendedSongsAtCloseWithClosingStore() {
        com.verby.indp.domain.store.Store store = Mockito.mock(com.verby.indp.domain.store.Store.class);
        StoreBusinessHour businessHour = Mockito.mock(StoreBusinessHour.class);
        given(businessHour.getStore()).willReturn(store);

        given(storeBusinessHourRepository.findByDayOfWeekAndCloseTimeBetween(
            org.mockito.ArgumentMatchers.anyInt(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        )).willReturn(List.of(businessHour));

        willDoNothing().given(playlistService).deleteRecommendedSongs(store);

        playlistScheduler.deleteRecommendedSongsAtClose();

        then(playlistService).should().deleteRecommendedSongs(store);
    }
}

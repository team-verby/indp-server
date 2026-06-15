package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest.SchedulePlaylistItem.SongItem;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPlaylistService {

    private final StoreService storeService;
    private final ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Transactional
    public void addScheduledPlaylists(SchedulePlaylistsUpdateRequest request) {
        // 같은 매장에 대해 다시 등록(재생성/적용 시각 변경)하면 아직 적용되지 않은(PENDING)
        // 기존 예약을 먼저 제거해, 이전 예약이 중복으로 적용되는 것을 방지한다.
        Set<Long> replacedStoreIds = new HashSet<>();
        request.schedulePlaylists()
            .forEach(schedulePlaylist -> {
                Store store = storeService.getStoreByName(schedulePlaylist.storeName());
                if (replacedStoreIds.add(store.getStoreId())) {
                    List<ScheduledPlaylist> pendings = scheduledPlaylistUpdateRepository
                        .findAllByStoreAndStatus(store, ScheduledPlaylist.UpdateStatus.PENDING);
                    if (!pendings.isEmpty()) {
                        scheduledPlaylistUpdateRepository.deleteAll(pendings);
                    }
                }
                List<SongItem> songItems = schedulePlaylist.songs();
                List<ScheduledPlaylistSong> songs = IntStream.range(0, songItems.size())
                    .mapToObj(i -> new ScheduledPlaylistSong(songItems.get(i).title(),
                        songItems.get(i).artist(), songItems.get(i).vid(),
                        songItems.get(i).playTime(), (double) (i + 1) * 10))
                    .toList();
                scheduledPlaylistUpdateRepository.save(
                    new ScheduledPlaylist(store, schedulePlaylist.scheduledAt(), songs));
            });
    }

}

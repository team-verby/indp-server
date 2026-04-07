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

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPlaylistService {

    private final StoreService storeService;
    private final ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Transactional
    public void addScheduledPlaylists(SchedulePlaylistsUpdateRequest request) {
        request.schedulePlaylists()
            .forEach(schedulePlaylist -> {
                Store store = storeService.getStoreById(schedulePlaylist.storeId());
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

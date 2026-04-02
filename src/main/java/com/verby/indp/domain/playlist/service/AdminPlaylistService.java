package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.request.SchedulePlaylistsUpdateRequest;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPlaylistService {
    private final PlaylistSongRepository playlistSongRepository;
    private final StoreService storeService;
    private final ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    @Transactional
    public void addScheduledPlaylists(SchedulePlaylistsUpdateRequest request) {
        request.schedulePlaylists()
            .forEach(schedulePlaylist -> {
                Store store = storeService.getStoreById(schedulePlaylist.storeId());
                List<ScheduledPlaylistSong> songs = schedulePlaylist.songs().stream()
                    .map(song -> new ScheduledPlaylistSong(song.title(), song.artist(), song.vid(), song.playTime(), song.playOrder()))
                    .toList();
                scheduledPlaylistUpdateRepository.save(new ScheduledPlaylist(store, schedulePlaylist.scheduledAt(), songs));
            });
    }
}

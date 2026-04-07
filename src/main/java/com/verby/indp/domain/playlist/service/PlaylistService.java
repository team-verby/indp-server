package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.common.exception.ServiceUnavailableException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistService {

    private static final int RECOMMENDATION_INSERT_OFFSET = 5;
    private static final double MIN_GAP = 0.0001;

    private final PlaylistSongRepository playlistSongRepository;
    private final ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;
    private final StoreService storeService;

    public FindStorePlaylistResponse getStorePlaylist(long storeId) {
        Store store = storeService.getStoreById(storeId);
        validateSubscribeActive(store);

        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return new FindStorePlaylistResponse(null, null);
        }

        List<PlaylistSong> songs = getSortedSongs(playlist.getPlaylistId());
        CurrentSong currentSong = CurrentSongResolver.resolveCurrentSong(store).orElse(null);
        return FindStorePlaylistResponse.from(songs, currentSong);
    }

    @Transactional
    public PlaylistSong addRecommendedSong(Store store, SongRecommendation recommendation) {
        validateSubscribeActive(store);

        Playlist playlist = store.getPlaylist();
        validatePlaylistExits(playlist);

        List<PlaylistSong> songs = getSortedSongs(playlist.getPlaylistId());

        double order = calculateOrder(store, songs);

        PlaylistSong playlistSong = new PlaylistSong(recommendation, true, recommendation.getVid(),
            recommendation.getPlayTime(),
            recommendation.getTitle(), recommendation.getArtist(), order);
        playlist.addSong(playlistSong);

        return playlistSong;
    }

    public List<PlaylistSong> getSortedSongs(long playlistId) {
        return playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlistId);
    }

    @Transactional
    public void applyDueScheduledUpdates() {
        List<ScheduledPlaylist> scheduledPlaylists = scheduledPlaylistUpdateRepository
            .findAllByStatusAndScheduledAtLessThanEqual(
                ScheduledPlaylist.UpdateStatus.PENDING,
                LocalDateTime.now()
            );

        for (ScheduledPlaylist scheduledPlaylist : scheduledPlaylists) {
            applyScheduledUpdate(scheduledPlaylist);
            scheduledPlaylist.markApplied();
        }
    }

    @Transactional
    public void deleteRecommendedSongs(Store store) {
        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return;
        }

        List<PlaylistSong> recommended = playlist.getSongs()
            .stream()
            .filter(PlaylistSong::isRecommended)
            .toList();

        playlistSongRepository.deleteAll(recommended);
    }

    private double calculateOrder(Store store, List<PlaylistSong> songs) {
        CurrentSong currentSong = getCurrentSong(store);

        Double prevOrder = null;
        Double nextOrder = null;
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getPlaylistSongId() == currentSong.playlistSongId()) {
                int nextSongIndex = Math.min(songs.size() - 1, i + RECOMMENDATION_INSERT_OFFSET);
                if (nextSongIndex == i) {
                    prevOrder = songs.get(i).getPlayOrder();
                } else {
                    nextOrder = songs.get(nextSongIndex).getPlayOrder();
                    prevOrder = songs.get(nextSongIndex - 1).getPlayOrder();

                    if (nextSongIndex - prevOrder < MIN_GAP) {
                        rebalancePlayOrder(songs);
                        nextOrder = songs.get(nextSongIndex).getPlayOrder();
                        prevOrder = songs.get(nextSongIndex - 1).getPlayOrder();
                    }
                }
                break;
            }
        }

        if (prevOrder == null) {
            return nextOrder - 1;
        }

        if (nextOrder == null) {
            return prevOrder + 1;
        }

        return (prevOrder + nextOrder) / 2;
    }

    private CurrentSong getCurrentSong(Store store) {
        return CurrentSongResolver.resolveCurrentSong(store)
            .orElseThrow(() -> new ServiceUnavailableException("음악 신청에 실패했습니다. 잠시 후 다시 시도해주세요."));
    }

    private void rebalancePlayOrder(List<PlaylistSong> songs) {
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setPlayOrder(i + 1);
        }
    }

    private void validatePlaylistExits(Playlist playlist) {
        if (playlist == null) {
            throw new NotFoundException("플레이리스트가 존재하지 않습니다.");
        }
    }

    private void applyScheduledUpdate(ScheduledPlaylist update) {
        Store store = update.getStore();
        List<ScheduledPlaylistSong> scheduledSongs = update.getSongs();
        List<PlaylistSong> songs = new ArrayList<>();
        for (int i = 0; i < scheduledSongs.size(); i++) {
            ScheduledPlaylistSong scheduledSong = scheduledSongs.get(i);
            PlaylistSong song = new PlaylistSong(null, false, scheduledSong.getVid(),
                scheduledSong.getPlayTime(),
                scheduledSong.getTitle(), scheduledSong.getArtist(), (i + 1) * 10.0);
            songs.add(song);
        }
        Playlist playlist = new Playlist(songs);
        store.assignPlaylist(playlist);
    }

    private void validateSubscribeActive(Store store) {
        if (store.isInactive()) {
            throw new BadRequestException("구독 정보가 없습니다.");
        }
    }
}

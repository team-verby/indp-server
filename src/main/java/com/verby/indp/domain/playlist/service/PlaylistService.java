package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylist;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
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

        List<PlaylistSong> songs = playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());
        CurrentSong currentSong = CurrentSongResolver.resolveCurrentSong(store).orElse(null);
        return FindStorePlaylistResponse.from(songs, currentSong);
    }

    @Transactional
    public PlaylistSong addRecommendedSong(Store store, SongRecommendation recommendation) {
        validateSubscribeActive(store);
        Playlist playlist = store.getPlaylist();
        List<PlaylistSong> songs = playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());

        int insertAfterIndex = resolveInsertIndex(store, songs);
        double[] positions = resolvePositions(songs, insertAfterIndex);

        if (positions[1] - positions[0] < 0.0001) {
            renormalize(songs);
            songs = playlistSongRepository
                .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());
            insertAfterIndex = resolveInsertIndex(store, songs);
            positions = resolvePositions(songs, insertAfterIndex);
        }

        double newPosition = (positions[0] + positions[1]) / 2.0;
        PlaylistSong playlistSong = new PlaylistSong(recommendation, true, recommendation.getVid(), recommendation.getPlayTime(),
                recommendation.getTitle(), recommendation.getArtist(), newPosition);
        playlist.addSong(playlistSong);

        return playlistSong;
    }

    @Transactional
    public void applyDueScheduledUpdates() {
        List<ScheduledPlaylist> due = scheduledPlaylistUpdateRepository
            .findAllByStatusAndScheduledAtLessThanEqual(
                ScheduledPlaylist.UpdateStatus.PENDING,
                LocalDateTime.now()
            );

        for (ScheduledPlaylist update : due) {
            applyScheduledUpdate(update);
            update.markApplied();
        }
    }

    @Transactional
    public void deleteRecommendedSongs(Store store) {
        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return;
        }

        List<PlaylistSong> recommended = playlistSongRepository
                .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId())
                .stream()
                .filter(PlaylistSong::isRecommended)
                .toList();

        playlistSongRepository.deleteAll(recommended);
    }

    private void applyScheduledUpdate(ScheduledPlaylist update) {
        Store store = update.getStore();
        List<ScheduledPlaylistSong> scheduledSongs = update.getSongs();
        List<PlaylistSong> songs = new ArrayList<>();
        for (int i = 0; i < scheduledSongs.size(); i++) {
            ScheduledPlaylistSong scheduledSong = scheduledSongs.get(i);
            PlaylistSong song = new PlaylistSong(null, false, scheduledSong.getVid(), scheduledSong.getPlayTime(),
                    scheduledSong.getTitle(), scheduledSong.getArtist(), (i + 1) * 10.0);
            songs.add(song);
        }
        Playlist playlist = new Playlist(songs);
        store.assignPlaylist(playlist);
    }

    private int resolveInsertIndex(Store store, List<PlaylistSong> songs) {
        int currentIndex = findCurrentSongIndex(store, songs);
        int insertAfterIndex = Math.min(
            currentIndex + RECOMMENDATION_INSERT_OFFSET - 1,
            songs.size() - 1
        );

        while (insertAfterIndex < songs.size() - 1
            && songs.get(insertAfterIndex + 1).isRecommended()) {
            insertAfterIndex++;
        }

        return insertAfterIndex;
    }

    private double[] resolvePositions(List<PlaylistSong> songs, int insertAfterIndex) {
        double prev = songs.get(insertAfterIndex).getPlayOrder();
        double next = insertAfterIndex + 1 < songs.size()
            ? songs.get(insertAfterIndex + 1).getPlayOrder()
            : prev + 1.0;
        return new double[]{prev, next};
    }

    private void renormalize(List<PlaylistSong> songs) {
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).updatePosition((i + 1) * 10.0);
        }
    }

    private int findCurrentSongIndex(Store store, List<PlaylistSong> songs) {
        long elapsedSeconds = CurrentSongResolver.calcElapsedSeconds(store);
        if (elapsedSeconds < 0) {
            return 0;
        }

        long cumulative = 0;
        for (int i = 0; i < songs.size(); i++) {
            long duration = (long) (songs.get(i).getPlayTime() != null ? songs.get(i).getPlayTime() : 0);
            if (cumulative + duration > elapsedSeconds) {
                return i;
            }
            cumulative += duration;
        }

        return songs.size() - 1;
    }

    private void validateSubscribeActive(Store store) {
        if (store.isInactive()) {
            throw new BadRequestException("구독 정보가 없습니다.");
        }
    }
}

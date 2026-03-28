package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylistSong;
import com.verby.indp.domain.playlist.ScheduledPlaylistUpdate;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistResponse;
import com.verby.indp.domain.playlist.repository.PlaylistRepository;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.playlist.repository.ScheduledPlaylistUpdateRepository;
import com.verby.indp.domain.recommendation.SongRecommendation;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.StoreBusinessHour;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistService {

    private static final int RECOMMENDATION_INSERT_OFFSET = 5;
    private final PlaylistRepository playlistRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final ScheduledPlaylistUpdateRepository scheduledPlaylistUpdateRepository;

    public FindStorePlaylistResponse getStorePlaylist(Store store, boolean isOwner) {
        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return new FindStorePlaylistResponse(isOwner, null, null);
        }

        List<PlaylistSong> songs = playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());

        if (songs.isEmpty()) {
            return new FindStorePlaylistResponse(isOwner, null,
                new FindStorePlaylistResponse.PlaylistInfo(0, 0, 0, List.of()));
        }

        FindStorePlaylistResponse.CurrentSongItem currentSong = resolveCurrentSong(store, songs);

        int recommendedCount = (int) songs.stream().filter(PlaylistSong::isRecommended).count();
        int totalPlayTime = songs.stream().mapToInt(s -> s.getPlayTime() != null ? s.getPlayTime() : 0).sum();

        FindStorePlaylistResponse.PlaylistInfo playlistInfo = getPlaylistInfo(songs, recommendedCount, totalPlayTime);

        return new FindStorePlaylistResponse(isOwner, currentSong, playlistInfo);
    }

    @Transactional
    public PlaylistSong addRecommendedSong(Store store, SongRecommendation recommendation) {
        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            playlist = playlistRepository.save(new Playlist());
            store.assignPlaylist(playlist);
        }

        List<PlaylistSong> songs = playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());

        if (songs.isEmpty()) {
            return playlistSongRepository.save(
                new PlaylistSong(playlist, recommendation, true, recommendation.getVid(), recommendation.getPlayTime(),
                    recommendation.getTitle(), recommendation.getArtist(), 1.0)
            );
        }

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

        return playlistSongRepository.save(
            new PlaylistSong(playlist, recommendation, true, recommendation.getVid(), recommendation.getPlayTime(),
                recommendation.getTitle(), recommendation.getArtist(), newPosition)
        );
    }

    @Transactional
    public void updateCurrentSong(Playlist playlist, long playlistSongId) {
        PlaylistSong song = getPlaylistSong(playlistSongId);
        playlist.updateCurrentSong(song);
    }

    @Transactional
    public void stopPlaylist(Playlist playlist) {
        playlist.stop();
    }

    @Transactional
    public void saveScheduledUpdate(Store store, LocalDateTime scheduledAt, List<ScheduledPlaylistSong> songs) {
        scheduledPlaylistUpdateRepository.save(new ScheduledPlaylistUpdate(store, scheduledAt, songs));
    }

    @Transactional
    public void applyDueScheduledUpdates() {
        List<ScheduledPlaylistUpdate> due = scheduledPlaylistUpdateRepository
            .findAllByStatusAndScheduledAtLessThanEqual(
                ScheduledPlaylistUpdate.UpdateStatus.PENDING,
                LocalDateTime.now()
            );

        for (ScheduledPlaylistUpdate update : due) {
            applyScheduledUpdate(update);
            update.markApplied();
        }
    }

    private void applyScheduledUpdate(ScheduledPlaylistUpdate update) {
        Store store = update.getStore();
        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            playlist = playlistRepository.save(new Playlist());
            store.assignPlaylist(playlist);
        }

        playlistSongRepository.deleteAllByPlaylist(playlist);
        playlist.stop();

        List<ScheduledPlaylistSong> songs = update.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            ScheduledPlaylistSong s = songs.get(i);
            playlistSongRepository.save(
                new PlaylistSong(playlist, null, false, s.getVid(), s.getPlayTime(),
                    s.getTitle(), s.getArtist(), (i + 1) * 10.0)
            );
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

    private PlaylistSong getPlaylistSong(long id) {
        return playlistSongRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않은 플레이리스트 곡입니다."));
    }

    private FindStorePlaylistResponse.PlaylistInfo getPlaylistInfo(List<PlaylistSong> songs, int recommendedCount, int totalPlayTime) {
        List<FindStorePlaylistResponse.SongItem> songItems = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            PlaylistSong song = songs.get(i);
            String refereeName = song.getSongRecommendation() != null
                    ? song.getSongRecommendation().getRefereeName() : null;
            songItems.add(new FindStorePlaylistResponse.SongItem(
                    song.getPlaylistSongId(), i + 1, song.getTitle(), song.getArtist(),
                    song.getPlayTime(), song.isRecommended(), refereeName
            ));
        }

        return new FindStorePlaylistResponse.PlaylistInfo(songs.size(), recommendedCount, totalPlayTime, songItems);
    }

    private FindStorePlaylistResponse.CurrentSongItem resolveCurrentSong(Store store, List<PlaylistSong> songs) {
        long elapsedSeconds = calcElapsedSeconds(store);
        if (elapsedSeconds < 0) {
            return null;
        }

        long cumulative = 0;
        for (int i = 0; i < songs.size(); i++) {
            PlaylistSong song = songs.get(i);
            long duration = song.getPlayTime();
            if (cumulative + duration > elapsedSeconds) {
                String refereeName = song.getSongRecommendation() != null
                    ? song.getSongRecommendation().getRefereeName() : null;
                return new FindStorePlaylistResponse.CurrentSongItem(
                    song.getPlaylistSongId(), i + 1, song.getTitle(), song.getArtist(),
                    song.getVid(), song.getPlayTime(), (int) (elapsedSeconds - cumulative),
                    song.isRecommended(), refereeName
                );
            }
            cumulative += duration;
        }

        return null;
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
        long elapsedSeconds = calcElapsedSeconds(store);
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

    private long calcElapsedSeconds(Store store) {
        LocalDateTime now = LocalDateTime.now();
        int todayDayOfWeek = now.getDayOfWeek().getValue();

        Optional<StoreBusinessHour> todayHour = store.getBusinessHours().stream()
            .filter(h -> h.getDayOfWeek() == todayDayOfWeek && !h.isClosed())
            .findFirst();

        if (todayHour.isEmpty()) {
            return -1;
        }

        LocalTime startTime = todayHour.get().getOpenTime().minusMinutes(30);
        LocalTime nowTime = now.toLocalTime();

        if (nowTime.isBefore(startTime)) {
            return -1;
        }

        return java.time.Duration.between(startTime, nowTime).getSeconds();
    }
}

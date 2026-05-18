package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.ForbiddenException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.CurrentSong;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.global.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerPlaylistService {

    private final StoreService storeService;
    private final SlackNotificationService slackNotificationService;
    private final PlaylistService playlistService;
    private final CurrentSongResolver currentSongResolver;

    public FindStorePlaylistByOwnerResponse getStorePlaylist(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);

        validateOwnership(store, owner);
        validateSubscribeActive(store);

        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return new FindStorePlaylistByOwnerResponse(null, null);
        }

        List<PlaylistSong> sortedSongs = playlistService.getSortedSongs(playlist.getPlaylistId());
        CurrentSong currentSong = currentSongResolver.resolveCurrentSong(store).orElse(null);
        return FindStorePlaylistByOwnerResponse.from(sortedSongs, currentSong);
    }

    public void regeneratePlaylist(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);

        validateOwnership(store, owner);
        validateSubscribeActive(store);

        slackNotificationService.handlePlaylistRegenerationRequest(store);
    }

    private void validateSubscribeActive(Store store) {
        if (store.isInactive()) {
            throw new BadRequestException("구독이 활성화되지 않았습니다.");
        }
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new ForbiddenException("접근할 수 없는 매장입니다.");
        }
    }
}

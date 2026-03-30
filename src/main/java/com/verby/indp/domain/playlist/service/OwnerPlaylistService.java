package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.auth.Owner;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.playlist.Playlist;
import com.verby.indp.domain.playlist.PlaylistSong;
import com.verby.indp.domain.playlist.dto.response.FindStorePlaylistByOwnerResponse;
import com.verby.indp.domain.playlist.repository.PlaylistSongRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import com.verby.indp.domain.subscription.service.SubscriptionService;
import com.verby.indp.global.slack.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerPlaylistService {
    private final PlaylistSongRepository playlistSongRepository;
    private final StoreService storeService;
    private final SubscriptionService subscriptionService;
    private final SlackNotificationService slackNotificationService;
    private final CurrentSongResolver currentSongResolver;

    public FindStorePlaylistByOwnerResponse getStorePlaylist(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);

        validateOwnership(store, owner);
        validateSubscribeActive(store);

        Playlist playlist = store.getPlaylist();
        if (playlist == null) {
            return new FindStorePlaylistByOwnerResponse(null, null);
        }

        List<PlaylistSong> songs = playlistSongRepository
            .findAllByPlaylistPlaylistIdOrderByPlayOrder(playlist.getPlaylistId());

        return FindStorePlaylistByOwnerResponse.from(songs, currentSongResolver.resolveCurrentSong(store, songs));
    }

    public void regeneratePlaylist(Owner owner, long storeId) {
        Store store = storeService.getStoreById(storeId);

        validateOwnership(store, owner);
        validateSubscribeActive(store);

        slackNotificationService.sendPlaylistRegenerateRequest(store);
    }

    private void validateSubscribeActive(Store store) {
        if (store.isInactive()) {
            throw new BadRequestException("구독 정보가 없습니다.");
        }
    }

    private void validateOwnership(Store store, Owner owner) {
        if (!store.getOwner().getOwnerId().equals(owner.getOwnerId())) {
            throw new NotFoundException("접근할 수 없는 매장입니다.");
        }
    }
}

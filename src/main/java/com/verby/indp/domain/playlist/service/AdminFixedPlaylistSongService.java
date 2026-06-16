package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.FixedPlaylistSong;
import com.verby.indp.domain.playlist.dto.request.CreateFixedPlaylistSongRequest;
import com.verby.indp.domain.playlist.dto.response.FindFixedPlaylistSongsResponse;
import com.verby.indp.domain.playlist.repository.FixedPlaylistSongRepository;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminFixedPlaylistSongService {

    private final StoreService storeService;
    private final FixedPlaylistSongRepository fixedPlaylistSongRepository;

    @Transactional
    public void addFixedPlaylistSong(CreateFixedPlaylistSongRequest request) {
        Store store = storeService.getStoreByName(request.storeName());
        FixedPlaylistSong song = new FixedPlaylistSong(
            store,
            request.startDate(),
            request.endDate(),
            request.hour(),
            request.position(),
            request.title(),
            request.artist(),
            request.vid(),
            request.playTime()
        );
        fixedPlaylistSongRepository.save(song);
    }

    public FindFixedPlaylistSongsResponse findFixedPlaylistSongs() {
        return FindFixedPlaylistSongsResponse.from(
            fixedPlaylistSongRepository.findAllByOrderByStartDateAscTargetHourAscPositionAsc());
    }

    public FindFixedPlaylistSongsResponse findActiveFixedPlaylistSongs(LocalDate date) {
        return FindFixedPlaylistSongsResponse.from(
            fixedPlaylistSongRepository
                .findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date));
    }

    @Transactional
    public void deleteFixedPlaylistSong(long fixedPlaylistSongId) {
        fixedPlaylistSongRepository.deleteById(fixedPlaylistSongId);
    }
}

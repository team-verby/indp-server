package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.MoodCatalog;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.SongItem;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.domain.playlist.repository.MusicCatalogSongRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMusicCatalogService {

    private final MusicCatalogSongRepository musicCatalogSongRepository;

    public FindMusicCatalogResponse findMusicCatalog() {
        return FindMusicCatalogResponse.from(
            musicCatalogSongRepository.findAllByOrderByMoodAscPositionAsc());
    }

    /**
     * 음원 카탈로그 전체를 통째로 교체한다(기존 전체 삭제 후 재삽입).
     * 프론트의 음원 데이터 관리(mdData)가 무드 카탈로그 전체를 한 번에 저장하는 방식과 1:1로 대응한다.
     */
    @Transactional
    public void updateMusicCatalog(UpdateMusicCatalogRequest request) {
        musicCatalogSongRepository.deleteAllInBatch();

        if (request.moods() == null) {
            return;
        }

        List<MusicCatalogSong> songs = new ArrayList<>();
        for (MoodCatalog moodCatalog : request.moods()) {
            if (moodCatalog == null || moodCatalog.mood() == null || moodCatalog.mood().isBlank()) {
                continue;
            }
            List<SongItem> items = moodCatalog.songs();
            if (items == null) {
                continue;
            }
            int position = 1;
            for (SongItem item : items) {
                if (item == null || item.title() == null || item.title().isBlank()) {
                    continue;
                }
                songs.add(new MusicCatalogSong(
                    moodCatalog.mood(),
                    position++,
                    item.title(),
                    item.artist(),
                    item.playTime(),
                    item.vid()
                ));
            }
        }
        musicCatalogSongRepository.saveAll(songs);
    }
}

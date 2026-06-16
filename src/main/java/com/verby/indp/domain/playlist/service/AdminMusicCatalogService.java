package com.verby.indp.domain.playlist.service;

import com.verby.indp.domain.playlist.MusicCatalogSong;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.MoodCatalog;
import com.verby.indp.domain.playlist.dto.request.UpdateMusicCatalogRequest.SongItem;
import com.verby.indp.domain.playlist.dto.response.FindMusicCatalogResponse;
import com.verby.indp.domain.playlist.repository.MusicCatalogSongRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMusicCatalogService {

    private final MusicCatalogSongRepository musicCatalogSongRepository;

    public FindMusicCatalogResponse findMusicCatalog() {
        List<MusicCatalogSong> songs =
            musicCatalogSongRepository.findAllByOrderByMoodAscPositionAsc();
        return FindMusicCatalogResponse.from(latestSavedAt(songs), songs);
    }

    /**
     * 음원 카탈로그 전체를 통째로 교체한다(기존 전체 삭제 후 재삽입).
     * 프론트의 음원 데이터 관리(mdData)가 무드 카탈로그 전체를 한 번에 저장하는 방식과 1:1로 대응한다.
     *
     * @return 저장된 시각(저장된 곡이 없으면 null). 전체 재삽입이라 행들의 createdAt 최댓값이 곧 마지막 저장 시각이다.
     */
    @Transactional
    public LocalDateTime updateMusicCatalog(UpdateMusicCatalogRequest request) {
        musicCatalogSongRepository.deleteAllInBatch();

        if (request.moods() == null) {
            return null;
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
        // saveAll 영속화 시 @CreatedDate(AuditingEntityListener)가 각 행 createdAt을 채운다.
        return latestSavedAt(songs);
    }

    private LocalDateTime latestSavedAt(List<MusicCatalogSong> songs) {
        return songs.stream()
            .map(MusicCatalogSong::getCreatedAt)
            .filter(Objects::nonNull)
            .max(Comparator.naturalOrder())
            .orElse(null);
    }
}

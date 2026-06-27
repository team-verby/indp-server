package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import com.verby.indp.domain.creator.dto.response.DjPlaylistDetailResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjPlaylistService {

    private final CreatorRepository creatorRepository;
    private final CreatorTrackRepository creatorTrackRepository;
    private final ListeningDailyRepository listeningDailyRepository;
    private final Clock clock;

    public FindDjPlaylistsResponse getPlaylists() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime threshold = now.minusSeconds(SettlementPolicy.ACTIVE_WINDOW_SEC);
        LocalDate today = now.toLocalDate();
        List<FindDjPlaylistsResponse.DjPlaylistItem> items = creatorRepository.findAll().stream()
            .filter(Creator::isActive)
            .map(creator -> {
                boolean live = creator.isLiveWithin(now, DjLiveService.LIVE_TTL_SEC);
                // 라이브 채널만 실시간 청취자 수를 집계한다(비라이브는 0, 불필요한 쿼리 회피).
                int listeners = live
                    ? listeningDailyRepository.countByCreatorIdAndYmdAndUpdatedAtGreaterThanEqual(
                        creator.getCreatorId(), today, threshold)
                    : 0;
                return FindDjPlaylistsResponse.DjPlaylistItem.from(creator, live, listeners);
            })
            .toList();
        return new FindDjPlaylistsResponse(items);
    }

    public DjPlaylistDetailResponse getPlaylistDetail(long creatorId) {
        Creator creator = creatorRepository.findById(creatorId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 채널입니다."));
        List<CreatorTrack> tracks = creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator);
        boolean live = creator.isLiveWithin(LocalDateTime.now(clock), DjLiveService.LIVE_TTL_SEC);
        return DjPlaylistDetailResponse.from(creator, tracks, live);
    }
}

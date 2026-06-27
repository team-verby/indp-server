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
import java.time.ZoneOffset;
import java.util.Random;
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
                int listeners;
                if (!live) {
                    listeners = 0;
                } else if (creator.isAutoLive()) {
                    // 임시 데모 채널(autoLive)은 채널별로 완만히 변하는 데모 청취자 수를 표시한다.
                    listeners = demoListeners(creator.getCreatorId(), now);
                } else {
                    // 실제 라이브 채널만 실시간 청취자 수를 집계한다.
                    listeners = listeningDailyRepository.countByCreatorIdAndYmdAndUpdatedAtGreaterThanEqual(
                        creator.getCreatorId(), today, threshold);
                }
                return FindDjPlaylistsResponse.DjPlaylistItem.from(creator, live, listeners);
            })
            .toList();
        return new FindDjPlaylistsResponse(items);
    }

    /**
     * 임시 데모 채널(autoLive)용 청취자 수.
     * 채널별 고정 베이스라인 + 시간에 따른 완만한 파동 + 5분 단위 소폭 지터로 0~1500 범위를 만든다.
     * 같은 시점·같은 채널이면 항상 같은 값(새로고침해도 불변), 시간이 지나면 자연스럽게 흐른다.
     */
    private static int demoListeners(long creatorId, LocalDateTime now) {
        long minutes = now.toEpochSecond(ZoneOffset.UTC) / 60;
        // 채널별 고정 베이스라인(약 250~1149)
        int base = 250 + Math.floorMod(Long.hashCode(creatorId * 0x9E3779B97F4A7C15L), 900);
        // 주기 약 30분, 진폭 ±120의 완만한 파동(채널별 위상차)
        double phase = (creatorId % 7) * 0.9;
        double wave = Math.sin((minutes / 30.0) * 2 * Math.PI + phase) * 120;
        // 5분 단위로 고정되는 소폭 지터(±25)
        long bucket = minutes / 5;
        int jitter = new Random(creatorId * 1000 + bucket).nextInt(51) - 25;
        long value = Math.round(base + wave + jitter);
        return (int) Math.max(0, Math.min(1500, value));
    }

    public DjPlaylistDetailResponse getPlaylistDetail(long creatorId) {
        Creator creator = creatorRepository.findById(creatorId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 채널입니다."));
        List<CreatorTrack> tracks = creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator);
        boolean live = creator.isLiveWithin(LocalDateTime.now(clock), DjLiveService.LIVE_TTL_SEC);
        return DjPlaylistDetailResponse.from(creator, tracks, live);
    }
}

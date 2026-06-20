package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.UpdateLiveTracksRequest;
import com.verby.indp.domain.creator.dto.response.DjLiveListenersResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import com.verby.indp.domain.listening.SettlementPolicy;
import com.verby.indp.domain.listening.repository.ListeningDailyRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjLiveService {

    /**
     * 라이브 TTL(초). 마지막 하트비트로부터 이 시간이 지나면 오프라인으로 간주한다.
     * 프론트 핑 주기(약 45초)의 3배여서 일시적 핑 유실에는 견딘다.
     */
    public static final long LIVE_TTL_SEC = 150;

    private final CreatorRepository creatorRepository;
    private final CreatorTrackRepository creatorTrackRepository;
    private final ListeningDailyRepository listeningDailyRepository;
    private final Clock clock;

    @Transactional
    public void startLive(Creator creator) {
        int trackCount = creatorTrackRepository.countByCreator(creator);
        if (trackCount == 0) {
            throw new BadRequestException("업로드된 트랙이 없으면 라이브를 시작할 수 없습니다.");
        }
        creator.startLive();
        creator.heartbeat(LocalDateTime.now(clock));
        creatorRepository.save(creator);
    }

    @Transactional
    public void heartbeat(Creator creator) {
        creator.heartbeat(LocalDateTime.now(clock));
        creatorRepository.save(creator);
    }

    @Transactional
    public void stopLive(Creator creator) {
        creator.stopLive();
        creatorRepository.save(creator);
    }

    public DjLiveListenersResponse getListeners(Creator creator) {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime threshold = now.minusSeconds(SettlementPolicy.ACTIVE_WINDOW_SEC);
        int count = listeningDailyRepository.countByCreatorIdAndYmdAndUpdatedAtGreaterThanEqual(
            creator.getCreatorId(), now.toLocalDate(), threshold);
        return new DjLiveListenersResponse(count);
    }

    @Transactional
    public void updateLiveTracks(Creator creator, UpdateLiveTracksRequest request) {
        // 라이브 트랙 순서 업데이트는 향후 구현 예정
    }
}

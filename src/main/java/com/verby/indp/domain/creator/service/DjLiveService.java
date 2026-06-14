package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.UpdateLiveTracksRequest;
import com.verby.indp.domain.creator.dto.response.DjLiveListenersResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjLiveService {

    private final CreatorRepository creatorRepository;
    private final CreatorTrackRepository creatorTrackRepository;

    @Transactional
    public void startLive(Creator creator) {
        int trackCount = creatorTrackRepository.countByCreator(creator);
        if (trackCount == 0) {
            throw new BadRequestException("업로드된 트랙이 없으면 라이브를 시작할 수 없습니다.");
        }
        creator.startLive();
        creatorRepository.save(creator);
    }

    @Transactional
    public void stopLive(Creator creator) {
        creator.stopLive();
        creatorRepository.save(creator);
    }

    public DjLiveListenersResponse getListeners(Creator creator) {
        return new DjLiveListenersResponse(0);
    }

    @Transactional
    public void updateLiveTracks(Creator creator, UpdateLiveTracksRequest request) {
        // 라이브 트랙 순서 업데이트는 향후 구현 예정
    }
}

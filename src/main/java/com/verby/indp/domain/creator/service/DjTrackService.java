package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.common.exception.BadRequestException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import com.verby.indp.domain.creator.dto.request.DjTrackUploadUrlRequest;
import com.verby.indp.domain.creator.dto.request.RegisterDjTrackRequest;
import com.verby.indp.domain.creator.dto.response.DjTrackResponse;
import com.verby.indp.domain.creator.dto.response.DjTrackUploadUrlResponse;
import com.verby.indp.domain.creator.dto.response.FindDjTracksResponse;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
import com.verby.indp.global.image.ImageService;
import com.verby.indp.global.image.PresignedUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjTrackService {

    // DJ 1인당 업로드 가능한 총 재생 길이 상한 (2시간). 비용(전송/저장) 안전장치이자 캐싱 친화 풀 크기 유지.
    private static final int MAX_TOTAL_SECS = 2 * 60 * 60;

    private final CreatorTrackRepository creatorTrackRepository;
    private final ImageService imageService;

    public FindDjTracksResponse getTracks(Creator creator) {
        List<CreatorTrack> tracks = creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator);
        return FindDjTracksResponse.from(tracks);
    }

    @Transactional
    public DjTrackResponse uploadTrack(Creator creator, MultipartFile file, String duration, int secs) {
        validateTotalLength(creator, secs);
        String streamUrl = imageService.uploadAudio(file);
        CreatorTrack track = new CreatorTrack(creator, file.getOriginalFilename(), streamUrl, duration, secs);
        return DjTrackResponse.from(creatorTrackRepository.save(track));
    }

    /**
     * S3 직접 업로드용 presigned URL을 발급한다. (브라우저 → S3, 서버를 거치지 않음)
     */
    public DjTrackUploadUrlResponse createUploadUrl(DjTrackUploadUrlRequest request) {
        PresignedUpload presigned = imageService.createAudioUploadUrl(request.filename());
        return new DjTrackUploadUrlResponse(presigned.uploadUrl(), presigned.streamUrl());
    }

    /**
     * S3 직접 업로드 완료 후, 트랙 메타데이터를 등록한다.
     */
    @Transactional
    public DjTrackResponse registerTrack(Creator creator, RegisterDjTrackRequest request) {
        validateTotalLength(creator, request.secs());
        CreatorTrack track = new CreatorTrack(
            creator, request.filename(), request.streamUrl(), request.duration(), request.secs());
        return DjTrackResponse.from(creatorTrackRepository.save(track));
    }

    /**
     * 신규 트랙을 더했을 때 누적 재생 길이가 상한(2시간)을 넘으면 거부한다.
     */
    private void validateTotalLength(Creator creator, int newSecs) {
        int currentSecs = creatorTrackRepository.sumSecsByCreator(creator);
        if (currentSecs + Math.max(newSecs, 0) > MAX_TOTAL_SECS) {
            int remain = Math.max(MAX_TOTAL_SECS - currentSecs, 0);
            throw new BadRequestException(
                "업로드 가능한 총 길이(2시간)를 초과했습니다. 남은 시간: " + (remain / 60) + "분 " + (remain % 60) + "초");
        }
    }

    @Transactional
    public void deleteTrack(Creator creator, long trackId) {
        CreatorTrack track = creatorTrackRepository.findById(trackId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 트랙입니다."));
        if (!track.getCreator().getCreatorId().equals(creator.getCreatorId())) {
            throw new AuthException("삭제 권한이 없습니다.");
        }
        creatorTrackRepository.delete(track);
    }
}

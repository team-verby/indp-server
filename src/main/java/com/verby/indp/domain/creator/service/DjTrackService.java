package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.AuthException;
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

    private final CreatorTrackRepository creatorTrackRepository;
    private final ImageService imageService;

    public FindDjTracksResponse getTracks(Creator creator) {
        List<CreatorTrack> tracks = creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator);
        return FindDjTracksResponse.from(tracks);
    }

    @Transactional
    public DjTrackResponse uploadTrack(Creator creator, MultipartFile file, String duration, int secs) {
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
        CreatorTrack track = new CreatorTrack(
            creator, request.filename(), request.streamUrl(), request.duration(), request.secs());
        return DjTrackResponse.from(creatorTrackRepository.save(track));
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

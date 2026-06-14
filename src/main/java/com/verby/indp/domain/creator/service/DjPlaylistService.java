package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.CreatorTrack;
import com.verby.indp.domain.creator.dto.response.DjPlaylistDetailResponse;
import com.verby.indp.domain.creator.dto.response.FindDjPlaylistsResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.domain.creator.repository.CreatorTrackRepository;
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

    public FindDjPlaylistsResponse getPlaylists() {
        List<Creator> creators = creatorRepository.findAll().stream()
            .filter(Creator::isActive)
            .toList();
        List<FindDjPlaylistsResponse.DjPlaylistItem> items = creators.stream()
            .map(FindDjPlaylistsResponse.DjPlaylistItem::from)
            .toList();
        return new FindDjPlaylistsResponse(items);
    }

    public DjPlaylistDetailResponse getPlaylistDetail(long creatorId) {
        Creator creator = creatorRepository.findById(creatorId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 채널입니다."));
        List<CreatorTrack> tracks = creatorTrackRepository.findAllByCreatorOrderByCreatedAtAsc(creator);
        return DjPlaylistDetailResponse.from(creator, tracks);
    }
}

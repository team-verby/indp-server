package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.ChangePasswordRequest;
import com.verby.indp.domain.creator.dto.request.UpdateDjProfileRequest;
import com.verby.indp.domain.creator.dto.response.DjProfileResponse;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.global.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DjService {

    private final CreatorRepository creatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public DjProfileResponse getProfile(Creator creator) {
        return DjProfileResponse.from(creator);
    }

    @Transactional
    public void updateProfile(Creator creator, UpdateDjProfileRequest request) {
        String thumbnailUrl = null;
        if (request.thumbnail() != null && !request.thumbnail().isEmpty()) {
            thumbnailUrl = imageService.uploadImage(request.thumbnail());
        }
        creator.updateProfile(request.djName(), thumbnailUrl);
        creatorRepository.save(creator);
    }

    @Transactional
    public void changePassword(Creator creator, ChangePasswordRequest request) {
        if (creator.mismatchPassword(request.currentPassword(), passwordEncoder)) {
            throw new AuthException("현재 비밀번호가 일치하지 않습니다.");
        }
        creator.changePassword(passwordEncoder.encode(request.newPassword()));
        creatorRepository.save(creator);
    }

    public Creator getCreatorById(long creatorId) {
        return creatorRepository.findById(creatorId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 크리에이터입니다."));
    }
}

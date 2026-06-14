package com.verby.indp.domain.creator.service;

import com.verby.indp.domain.common.exception.ConflictException;
import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.creator.Creator;
import com.verby.indp.domain.creator.dto.request.CreateCreatorRequest;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse;
import com.verby.indp.domain.creator.dto.response.FindCreatorsResponse.CreatorItem;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCreatorService {

    private final CreatorRepository creatorRepository;

    @Transactional
    public void createCreator(CreateCreatorRequest request) {
        if (creatorRepository.existsByEmail(request.email())) {
            throw new ConflictException("이미 사용 중인 이메일입니다.");
        }
        Creator creator = new Creator(
            request.name(),
            request.djName(),
            request.phone(),
            request.email(),
            request.password()
        );
        creatorRepository.save(creator);
    }

    public FindCreatorsResponse findCreators() {
        List<CreatorItem> items = creatorRepository.findAll().stream()
            .map(CreatorItem::from)
            .toList();
        return new FindCreatorsResponse(items);
    }

    @Transactional
    public void deactivate(long creatorId) {
        Creator creator = creatorRepository.findById(creatorId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 크리에이터입니다."));
        creator.deactivate();
    }
}

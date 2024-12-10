package com.verby.indp.domain.region.service;

import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.store.dto.response.FindRegionsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public FindRegionsResponse findRegions() {
        List<Region> regions = regionRepository.findAllByOrderBySequenceAsc();
        return FindRegionsResponse.from(regions);
    }
}

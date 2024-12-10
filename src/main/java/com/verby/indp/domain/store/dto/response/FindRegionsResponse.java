package com.verby.indp.domain.store.dto.response;

import com.verby.indp.domain.region.Region;
import java.util.List;

public record FindRegionsResponse(List<String> regions) {

    public static FindRegionsResponse from(List<Region> regions) {
        List<String> stringRegions = regions.stream()
            .map(Region::getRegion)
            .toList();

        return new FindRegionsResponse(stringRegions);
    }
}

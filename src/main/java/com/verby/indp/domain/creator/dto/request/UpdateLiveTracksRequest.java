package com.verby.indp.domain.creator.dto.request;

import java.util.List;

public record UpdateLiveTracksRequest(
    List<Long> trackIds
) {

}

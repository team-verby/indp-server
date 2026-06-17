package com.verby.indp.domain.creator.dto.request;

public record RegisterDjTrackRequest(
    String filename,
    String streamUrl,
    String duration,
    int secs
) {

}

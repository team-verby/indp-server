package com.verby.indp.global.image;

public record PresignedUpload(
    String uploadUrl,
    String streamUrl
) {

}

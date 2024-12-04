package com.verby.indp.global.image.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageRequest(
    MultipartFile image
) {

}

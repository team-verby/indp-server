package com.verby.indp.domain.creator.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UpdateDjProfileRequest(
    String djName,
    MultipartFile thumbnail
) {

}

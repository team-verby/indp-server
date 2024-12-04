package com.verby.indp.global.image;

import com.verby.indp.global.image.dto.ImageRequest;
import com.verby.indp.global.image.dto.ImageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImageResponse> uploadImages(
        @ModelAttribute ImageRequest request
    ) {
        return ResponseEntity.ok(new ImageResponse(imageService.uploadImage(request.image())));
    }

}

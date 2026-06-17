package com.verby.indp.global.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.verby.indp.domain.common.exception.BadRequestException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private AmazonS3 amazonS3;

    @Nested
    @DisplayName("uploadImage 메서드 실행 시")
    class UploadImage {

        @Test
        @DisplayName("성공 : 이미지가 업로드된다.")
        void uploadImage() throws MalformedURLException {
            // given
            MockMultipartFile file = new MockMultipartFile("image", "image.png", IMAGE_PNG_VALUE,
                "content".getBytes());

            when(amazonS3.getUrl(any(), any())).thenReturn(
                new URL("https://s3.amazonaws.com/imageUrl"));

            // when
            imageService.uploadImage(file);

            // then
            verify(amazonS3, times(1)).putObject(any());
            verify(amazonS3, times(1)).getUrl(any(), any());
        }
    }

    @Nested
    @DisplayName("createAudioUploadUrl 메서드 실행 시")
    class CreateAudioUploadUrl {

        @Test
        @DisplayName("성공 : presigned 업로드 URL과 CloudFront 스트리밍 URL을 반환한다.")
        void createAudioUploadUrl() throws MalformedURLException {
            // given
            org.springframework.test.util.ReflectionTestUtils.setField(
                imageService, "cloudFrontDomain", "d144jf8wjk9lk.cloudfront.net");
            when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class)))
                .thenReturn(new URL("https://s3.amazonaws.com/audio/uuid-track.mp3?signature=x"));

            // when
            PresignedUpload result = imageService.createAudioUploadUrl("track.mp3");

            // then
            assertThat(result.uploadUrl()).contains("signature");
            assertThat(result.streamUrl()).startsWith("https://d144jf8wjk9lk.cloudfront.net/audio/");
            assertThat(result.streamUrl()).endsWith("-track.mp3");
            verify(amazonS3, times(1)).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
        }

        @Test
        @DisplayName("실패 : 파일명이 비어 있으면 예외를 던진다.")
        void createAudioUploadUrlWithBlankFilename() {
            assertThatThrownBy(() -> imageService.createAudioUploadUrl("  "))
                .isInstanceOf(BadRequestException.class);
        }
    }

}

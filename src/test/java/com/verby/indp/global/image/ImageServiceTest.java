package com.verby.indp.global.image;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.amazonaws.services.s3.AmazonS3;
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
            MockMultipartFile file = new MockMultipartFile("image", "image.png", IMAGE_PNG_VALUE, "content".getBytes());

            when(amazonS3.getUrl(any(), any())).thenReturn(new URL("https://s3.amazonaws.com/imageUrl"));

            // when
            imageService.uploadImage(file);

            // then
            verify(amazonS3, times(1)).putObject(any());
            verify(amazonS3, times(1)).getUrl(any(), any());
        }
    }

}

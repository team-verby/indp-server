package com.verby.indp.global.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.verby.indp.domain.common.exception.BadRequestException;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private static final String IMAGE_FOLDER = "image";
    private static final String AUDIO_FOLDER = "audio";

    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile) {
        return upload(multipartFile, IMAGE_FOLDER);
    }

    public String uploadAudio(MultipartFile multipartFile) {
        return upload(multipartFile, AUDIO_FOLDER);
    }

    private String upload(MultipartFile multipartFile, String folder) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String key = folder + "/" + multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata));
            return amazonS3.getUrl(bucket, key).toString();
        } catch (IOException e) {
            throw new BadRequestException("파일을 업로드하는데 실패했습니다.");
        }
    }
}

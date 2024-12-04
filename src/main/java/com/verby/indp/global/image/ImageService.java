package com.verby.indp.global.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
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

    private static final String FOLDER_NAME = "image";

    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(
                new PutObjectRequest(bucket + "/" + FOLDER_NAME, multipartFile.getOriginalFilename(), inputStream,
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucket + "/" + FOLDER_NAME, multipartFile.getOriginalFilename()).toString();
        } catch (IOException exception) {
            throw new BadRequestException("이미지를 업로드하는데 실패했습니다.");
        }
    }

}

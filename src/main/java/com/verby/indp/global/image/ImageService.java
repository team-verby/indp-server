package com.verby.indp.global.image;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.verby.indp.domain.common.exception.BadRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    private static final String IMAGE_FOLDER = "image";
    private static final String AUDIO_FOLDER = "audio";
    private static final long PRESIGNED_URL_EXPIRE_MILLIS = 1000L * 60 * 5; // 5분

    private final AmazonS3 amazonS3;

    public String uploadImage(MultipartFile multipartFile) {
        return upload(multipartFile, IMAGE_FOLDER);
    }

    public String uploadAudio(MultipartFile multipartFile) {
        return upload(multipartFile, AUDIO_FOLDER);
    }

    /**
     * 브라우저가 S3로 직접 업로드할 수 있는 presigned PUT URL을 발급한다.
     * 큰 오디오 파일이 서버(EC2/nginx)를 거치지 않으므로 업로드 용량 제한과 서버 부하를 피한다.
     */
    public PresignedUpload createAudioUploadUrl(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new BadRequestException("filename은 필수입니다.");
        }
        // URL에 안전하도록 파일명 정리(공백·특수문자 → _). 표시용 원본명은 DB에 별도 저장됨.
        String safeName = filename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String key = AUDIO_FOLDER + "/" + UUID.randomUUID() + "-" + safeName;
        Date expiration = new Date(System.currentTimeMillis() + PRESIGNED_URL_EXPIRE_MILLIS);

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration);

        URL uploadUrl = amazonS3.generatePresignedUrl(request);
        // 재생은 CloudFront를 통해 — 버킷은 비공개 유지(OAC), 캐싱으로 전송비 절감
        String streamUrl = toCloudFrontUrl(key);
        return new PresignedUpload(uploadUrl.toString(), streamUrl);
    }

    private String toCloudFrontUrl(String key) {
        return "https://" + cloudFrontDomain + "/" + UriUtils.encodePath(key, StandardCharsets.UTF_8);
    }

    private String upload(MultipartFile multipartFile, String folder) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        String key = folder + "/" + multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata));
            // 버킷은 비공개(OAC) — 공개 접근은 CloudFront 경유로만. raw S3 URL은 403이므로 CDN URL 반환.
            return toCloudFrontUrl(key);
        } catch (IOException e) {
            throw new BadRequestException("파일을 업로드하는데 실패했습니다.");
        }
    }
}

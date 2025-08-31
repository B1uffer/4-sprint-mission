package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

public class S3BinaryContentStorage implements BinaryContentStorage {

    private final String accessKey;
    private final String secretKey;
    private final String region;
    private final String bucket;
    private final long presignedUrlExpirationSeconds;

    private S3Client s3;
    private S3Presigner presigner;

    public S3BinaryContentStorage(
            String accessKey, String secretKey, String region, String bucket,
            long presignedUrlExpirationSeconds
    ) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.presignedUrlExpirationSeconds = presignedUrlExpirationSeconds;
    }

    @Override
    public UUID put(UUID id, byte[] content) {
        getS3().putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey(id))
                        .contentType("application/octet-stream")
                        .build(),
                RequestBody.fromBytes(content)
        );
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        ResponseInputStream<GetObjectResponse> in = getS3().getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey(id))
                        .build()
        );
        return in; // 호출 측에서 close
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        String url = presignedGetUrl( // presignedGetUrl 생성
                objectKey(dto.id()),
                dto.contentType(),
                dto.fileName()
        );
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }

    /**
     * 밑은 유틸리티
     */

    private String objectKey(UUID id) {
        return "binary/" + id;
    }

    private S3Client getS3() {
        if (s3 == null) {
            synchronized (this) {
                if (s3 == null) {
                    var creds = AwsBasicCredentials.create(accessKey, secretKey);
                    s3 = S3Client.builder()
                            .credentialsProvider(StaticCredentialsProvider.create(creds))
                            .region(Region.of(region))
                            .build();
                }
            }
        }
        return s3;
    }

    private S3Presigner getPresigner() {
        if (presigner == null) {
            synchronized (this) {
                if (presigner == null) {
                    var creds = AwsBasicCredentials.create(accessKey, secretKey);
                    presigner = S3Presigner.builder()
                            .credentialsProvider(StaticCredentialsProvider.create(creds))
                            .region(Region.of(region))
                            .build();
                }
            }
        }
        return presigner;
    }

    /**
     * GET Presigned URL 생성.
     * - contentType이 있으면 응답 Content-Type 지정
     * - fileName이 있으면 Content-Disposition=attachment; filename="..." 지정
     * 하.. 이게 대체 뭔소리야? 다시 정리해서 하나하나 뜯은 후 스토리에 정리하기
     */
    private String presignedGetUrl(String key, String contentType, String fileName) {
        GetObjectRequest.Builder get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key);

        if (contentType != null && !contentType.isBlank()) {
            get = get.responseContentType(contentType);
        }
        if (fileName != null && !fileName.isBlank()) {
            String safe = fileName.replaceAll("[\\r\\n\"\\\\]", "_");
            get = get.responseContentDisposition("attachment; filename=\"" + safe + "\"");
        }

        PresignedGetObjectRequest pre = getPresigner().presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
                        .getObjectRequest(get.build())
                        .build()
        );
        return pre.url().toString();
    }
}

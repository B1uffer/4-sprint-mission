package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.*; // JUnit 5 API
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AWSS3Test { // 테스트 메서드

    private static Properties env;
    private static S3Client s3;
    private static S3Presigner s3Presigner;

    private static String bucket;
    private static String region;

    @BeforeAll
    static void setUpAll() throws IOException {
        env = loadEnvFromClassPath();
        String accessKey = env.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = env.getProperty("AWS_S3_SECRET_KEY");
        region = env.getProperty("AWS_S3_REGION");
        bucket = env.getProperty("AWS_S3_BUCKET");

        var creds = AwsBasicCredentials.create(accessKey, secretKey); // credentials
        var reg = Region.of(region); // regions

        s3 = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(reg)
                .build();

        s3Presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .region(reg)
                .build();

        // 버킷이 없으면 실패함
        s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    }

    @AfterAll
    static void tearDownAll() {
        if(s3Presigner != null) {
            s3Presigner.close();
        }
        if(s3 != null) {
            s3.close();
        }
    }

    /**
     * 업로드
     */

    @Test
    @DisplayName("업로드")
    void upload() {
        String key = "test-" + System.currentTimeMillis() + ".txt";
        String content = "test Hello, S3";

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key) // 가짜 key
                .contentType("text/plain; charset=utf-8")
                .build();

        s3.putObject(put, RequestBody.fromString(content, StandardCharsets.UTF_8));

        HeadObjectResponse head = s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());

        Assertions.assertEquals("text/plain; charset=utf-8", head.contentType());
        System.out.println("upload ok s3 ://" + bucket + "/" + key);
    }

    /**
     *  다운로드
     */

    @Test
    @DisplayName("다운로드")
    void download() throws IOException {
        String key = "test-" + System.currentTimeMillis() + ".txt";
        String content = "Download Test content";

        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
                RequestBody.fromString(content, StandardCharsets.UTF_8));

        GetObjectRequest get = GetObjectRequest.builder().bucket(bucket).key(key).build();

        try(ResponseInputStream<GetObjectResponse> ins = s3.getObject(get)) {
            String downloaded = new String(ins.readAllBytes(), StandardCharsets.UTF_8); // readAllBytes
            Assertions.assertEquals(content, downloaded);
            System.out.println("download ok " + downloaded);
        }
    }

    /**
     * PresignedURL 생성
     */
    @Test
    @DisplayName("Presigned URL put, get 생성")
    void createPresignedURL() {
        String key = "test-" + System.currentTimeMillis() + ".txt";

        // 업로드용, put
        PresignedPutObjectRequest putUrl = s3Presigner.presignPutObject(
                PutObjectPresignRequest.builder() // PutObjectPresignRequest
                        .signatureDuration(Duration.ofMinutes(10))
                        .putObjectRequest(PutObjectRequest.builder() // putObjectRequest
                                .bucket(bucket).key(key)
                                .contentType("text/plain")
                                .build())
                        .build()
        ); // PresignedPutObjectRequest 인스턴스 생성 끝

        // 다운로드용, get
        PresignedGetObjectRequest getUrl = s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()  // getObjectPresignRequest
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(GetObjectRequest.builder() // getObjectRequest
                                .bucket(bucket).key(key).build())
                        .build()
        );

        Assertions.assertTrue(putUrl.url().toString().startsWith("http"));
        Assertions.assertTrue(getUrl.url().toString().startsWith("http"));
        System.out.println("presigned put" + putUrl.url());
        System.out.println("presigned get" + getUrl.url());
    }

    /**
     * 밑은 유틸리티
     */

    private static Properties loadEnvFromClassPath() throws IOException {

        String[] candidates = {".env","config/.env","aws/.env"};

        for(String name : candidates) {
            try(InputStream in = AWSS3Test.class.getClassLoader().getResourceAsStream(name)) {
                if(in != null) {
                    System.out.println("debug: Loaded from classpath : " + name);
                    return parseEnv(in);
                }
            }
        } // for문 끝
        throw new IOException("클래스 패스에서 .env를 찾지 못했습니다. 시도 경로 : .env, config/.env, aws/.env");
    }

    private static Properties parseEnv(InputStream in) throws IOException {
        Properties properties = new Properties();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if(trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                int index = trimmed.indexOf('=');
                if(index <= 0) {
                    continue;
                }

                String key = trimmed.substring(0, index).trim();
                String value = trimmed.substring(index + 1).trim();

                // 따옴표 제거
                if((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }

                properties.setProperty(key, value);
            } // while 끝
        } // try 끝
        return properties;
    } // loadDotEnv 끝

    private static String require(Properties p, String key) {
        String v = p.getProperty(key);
        if(v == null || v.isBlank()) {
            throw new IllegalStateException("필수 설정 누락" + key);
        }
        return v.trim();
    }
}




/* .env에서 값이 잘 들어오나 테스트할때 썼던거 */

//    public static void main(String[] args) throws IOException {
//        Properties env = loadEnvFromClassPath();
//        System.out.println("Access Key : " + env.getProperty("AWS_S3_ACCESS_KEY"));
//        System.out.println("Secret Key : " + env.getProperty("AWS_S3_SECRET_KEY"));
//        System.out.println("Region : " + env.getProperty("AWS_S3_REGION"));
//        System.out.println("Bucket : " + env.getProperty("AWS_S3_BUCKET"));
//    }

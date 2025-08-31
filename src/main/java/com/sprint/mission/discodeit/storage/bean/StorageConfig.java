package com.sprint.mission.discodeit.storage.bean;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "discodeit.storage.s3")
class S3Props {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private long presignedUrlExpiration = 600; // 기본 10분
}

@Configuration
@EnableConfigurationProperties(S3Props.class)
public class StorageConfig {

    /**
     * application.yaml (혹은 .env) 에서
     * discodeit.storage.type=s3 인 경우에만
     * S3BinaryContentStorage Bean 을 등록한다.
     */
    @Bean
    @ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
    public BinaryContentStorage s3BinaryContentStorage(S3Props props) {
        return new S3BinaryContentStorage(
                props.getAccessKey(),
                props.getSecretKey(),
                props.getRegion(),
                props.getBucket(),
                props.getPresignedUrlExpiration()
        );
    }
}

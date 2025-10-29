package com.sprint.mission.discodeit.config;

import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;

@Component
@EnableRetry
public class RetryConfig {
    // Spring Retry 활성화하기
}

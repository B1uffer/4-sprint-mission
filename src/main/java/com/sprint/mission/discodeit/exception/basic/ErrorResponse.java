package com.sprint.mission.discodeit.exception.basic;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse { // DTO
    Instant timestamp;
    String code;
    String message;
    Map<String, Object> details;
    String exceptionType; // 발생한 예외의 클래스 이름
    int status; // HTTP 상태코드
}

/**
 * ErrorResponse와 @RestController를 활용해서 예외를 처리하는 핸들러를 구현하는데,
 * 모든 핸들러는 일관된 응답(ErrorResponse)을 가져야함. 즉 위의 ErrorResponse 형태를 가져야함
 */

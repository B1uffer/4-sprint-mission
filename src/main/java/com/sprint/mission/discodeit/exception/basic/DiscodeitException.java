package com.sprint.mission.discodeit.exception.basic;

import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DiscodeitException extends RuntimeException{ // 모든 예외의 기본이 되는 DiscodeitException
    private final Instant timestamp;
    private ErrorCode errorCode;
    private Map<String, Object> details = new HashMap<>();

    /**
     * details는 예외 발생 상황에 대한 추가 정보를 저장하기 위한 속성
     * 예 : 조회를 시도한 사용자의 ID 정보
     * 예 : 업데이트를 시도한 PRIVATE 채널의 ID 정보
     */

    public DiscodeitException(ErrorCode errorCode) {
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    public DiscodeitException(String message) {
        super(message);
        this.timestamp = Instant.now();
    }

}

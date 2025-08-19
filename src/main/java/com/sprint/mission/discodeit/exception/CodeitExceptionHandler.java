package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.exception.basic.ErrorResponse;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class CodeitExceptionHandler {
    /**
     * GlobalExceptionHandler 참고 활용
     */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) { // UserNotFound
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(Instant.now())
                .code("USER_NOT_FOUND")
                .message(e.getMessage())
                .details(null)
                .exceptionType(e.getClass().getName()) // 발생한 예외의 클래스 이름
                .status(HttpStatus.NOT_FOUND.value()) // HTTP 상태코드
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

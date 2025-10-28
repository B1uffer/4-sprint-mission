package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.NotificationGetRequest;
import com.sprint.mission.discodeit.dto.request.NotificationOkReqeust;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationApi {

    @Operation(summary = "Notification 알림 조회 생성")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification 조회가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Notification ErrorResponse 발생",
                    content = @Content(examples = @ExampleObject(value = "Notification not found"))
            )
    })
    @GetMapping
    ResponseEntity<List<NotificationDto>> getAllNotifications(NotificationGetRequest request); // 알림 조회

    @Operation(summary = "Notification 알림 확인, 삭제과정")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Notification 알림 확인, 삭제됨"),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "인가 실패, 타인의 알림",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
              responseCode = "404",
              description = "알림이 없음",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{notificationId}")
    ResponseEntity<Void> okNotificationDelete(NotificationOkReqeust request); // 알림 확인
}

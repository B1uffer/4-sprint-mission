package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.request.NotificationGetRequest;
import com.sprint.mission.discodeit.dto.request.NotificationOkReqeust;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    // 알림 조회하기
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications(
            NotificationGetRequest request) {
        List<NotificationDto> listNoty = notificationService.getNotification(request.userId());
        return ResponseEntity.ok(listNoty);
    }

    // 알림 확인하기
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> okNotificationDelete(NotificationOkReqeust request) {
        notificationService.okNotification(request.notificationId(), request.userId());
        return ResponseEntity.noContent().build();
    }
}

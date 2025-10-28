package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> getNotification(UUID id); // 알림 조회
    void okNotification(UUID notificationId, UUID userId); // 알림 확인
}

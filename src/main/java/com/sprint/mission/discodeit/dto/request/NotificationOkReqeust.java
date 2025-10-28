package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

// 알림 확인, 컨트롤러에서 받는 리퀘스트
public record NotificationOkReqeust(
        UUID notificationId,
        UUID userId
) {
}

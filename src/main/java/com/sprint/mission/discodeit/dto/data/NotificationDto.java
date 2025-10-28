package com.sprint.mission.discodeit.dto.data;

import java.time.Instant;
import java.util.UUID;

public record NotificationDto( // 알림 API 구현에 필요한 dto
        UUID id,
        Instant createdAt,
        UUID receiverId, // 알림을 수신할 User의 id
        String title,
        String content
) {
}

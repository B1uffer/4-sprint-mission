package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    @Transactional
    @Override
    public List<NotificationDto> getNotification(UUID userId) { // 알람 조회, userId는 reciveredId
        List<Notification> notification = notificationRepository.findAllByReceiverId(userId);
        return mapper.toDto(notification);
    }

    @Transactional
    @Override
    public void okNotification(UUID notificationId, UUID userId) { // 알람 확인, 알람을 삭제하기
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("Notification not found"));

        if(!notification.getReceiverId().equals(userId)) {
            throw new NoSuchElementException("Notification receiver not found");
        }
        notificationRepository.delete(notification);
    }
}

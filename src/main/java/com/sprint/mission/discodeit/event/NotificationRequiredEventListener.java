package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class NotificationRequiredEventListener {
    // 알림이 필요한 이벤트가 발행되었을 때 알림을 생성하기
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final NotificationRepository notificationRepository;

    public NotificationRequiredEventListener(UserRepository userRepository, ChannelRepository channelRepository, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * 해당 채널의 알림 여부를 활성화한 ReadStatus 조회하기
     * 해당 ReadStatus의 사용자들에게 알림을 생성하기
     * 해당 메시지를 보낸 사람은 알림 대상에서 제외하기
     */
    @Async
    @TransactionalEventListener
    public void on(MessageCreatedEvent event) {
        System.out.println("onMessageCreatedEvent");
        Channel channel = channelRepository.findById(event.getMessage().getId())
                .orElseThrow(() -> new ChannelNotFoundException());
        User user = userRepository.findById(event.getMessage().getId())
                .orElseThrow(() -> new UserNotFoundException());
        Message message = event.getMessage();

        Notification notification = new Notification(user,
                "title: \"보낸 사람 (#" + channel.getName() + ")\"",
                "content: \"" + message.getContent() + "\"");

        notificationRepository.save(notification);
        log.info("알림 생성완료 : notification = {}",notification);
    }

    /**
     * 권한이 변경된 당사자에게 알림을 생성하기
     */
    @Async
    @TransactionalEventListener
    public void on(RoleUpdatedEvent event) {
        User user = userRepository.findById(event.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException());

        Notification notification = new Notification(user,
                "title : \"권한이 변경되었습니다.\"",
                "content: \"USER -> CHANNEL_MANAGER\"");
        notificationRepository.save(notification);
        log.info("권한알림 생성완료 : notification : {}", notification);
    }
}

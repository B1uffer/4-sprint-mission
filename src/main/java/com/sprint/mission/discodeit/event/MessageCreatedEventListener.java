package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MessageCreatedEventListener {
    private final ReadStatusRepository readStatusRepository;

    public MessageCreatedEventListener(ReadStatusRepository readStatusRepository) {
        this.readStatusRepository = readStatusRepository;
    }

    /**
     * PRIVATE 채널은 알림 여부를 true로 초기화하기
     * PUBLIC 채널은 알림 여부를 false로 초기화하기
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void handleMessageCreatedEvent(MessageCreatedEvent event) {
        System.out.println("☆MessageCreatedEventListener received event☆: " + event.getMessage());

        ReadStatus readStatus = readStatusRepository.findById(event.getMessage().getId())
                .orElseThrow(() -> new MessageNotFoundException());
        ChannelType type = event.getMessage().getChannel().getType();

        if(type == ChannelType.PRIVATE) {
            readStatus.setNotificationEnabled(true);
        } else {
            readStatus.setNotificationEnabled(false);
        }
        readStatusRepository.save(readStatus);
    }
}

package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Getter;

@Getter
public class MessageCreatedEvent {
    /**
     * 1) 채널에 새로운 메시지가 등록되거나
     * 2) 권한이 변경된 경우
     * 이벤트를 발행해 알림을 받을 수 있도록 구현하기
     */
    private final Message message;

    public MessageCreatedEvent(Message message) {
        this.message = message;

        System.out.println("MessageCreatedEvent activated");
    }
}

package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface messageService {

    // 비즈니스 로직, 도메인 모델 생성 CRUD

    // CRUD 각각 받아야하는 파라미터와 리턴타입에 대해 고민하는게 먼저다..

    public Message createMessage(UUID userId, UUID channelId, String content);
    
    public Message readMessage(UUID messageId);
    
    public Message updateMessage(UUID messageId, String newContent);
    
    public Message deleteMessage(UUID messageId);
    
}

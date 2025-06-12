package com.sprint.mission.discodeit.run;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.messageService;

import java.util.UUID;

public class TestClass {

    static void messageCRUDTest(messageService messageService) {

        UUID channelID = UUID.randomUUID();
        UUID userID = UUID.randomUUID();

        // 메세지 생성
        Message message = messageService.createMessage(channelID, userID, "Hello, World!");
        System.out.println("메세지 생성 : " + message.getContent());

        // 메세지 조회
        Message searchMessage = messageService.readMessage(message.getMessageId());
        System.out.println("메세지 조회 : " + searchMessage.getChannelId());

        // 메세지 수정
        Message updateMessage = messageService.updateMessage(message.getMessageId(), "안녕!!!!");
        System.out.println("메세지 수정 : " + updateMessage.getMessageId());

        // 메세지 삭제
        messageService.deleteMessage(message.getMessageId());

        System.out.println("메세지가 삭제되었습니다.");
    }

}

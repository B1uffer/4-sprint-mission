package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.messageService;

import java.util.*;

public class JCFMessageService implements messageService {

    private final Map<UUID, Message> messageData;
    // 메세지 데이터를 저장할 필드를 만들고,

    public JCFMessageService() {

        this.messageData = new HashMap<>();
        // 생성자를 통해 불러올때마다 초기화한다
    }

    public Message createMessage(UUID userId, UUID channelId, String content) {
        //메세지 생성

        Message message = new Message(userId, channelId, content);
        // 파라미터로 받은 값을 전부 Message 인스턴스에 넣는다
        // Message에 새로 생성된 userId와 channelID가 들어가있다

        this.messageData.put(channelId, message);
        //Map이니까 add가 아니라 put을 쓴다

        return message;

    }

    public Message readMessage(UUID searchId) {
        // 메세지 찾기



        if(messageData.containsKey(searchId)) {

            Message findMessage = this.messageData.get(searchId);

            return findMessage;

        }

        /*

        https://it-hhhj2.tistory.com/92

        Message messageNullable = this.data.get(messageId);

        return Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

         */

        return null;

    }

    public Message updateMessage(UUID messageId, String newContent) {
        Message updateMessage = this.messageData.get(messageId);

        if(newContent != null && !newContent.equals(updateMessage.getContent())) {
            updateMessage.updateContent(newContent);
        }

        return updateMessage;
    }

    public Message deleteMessage(UUID messageId) {

        if(!this.messageData.containsKey(messageId)) {

            throw new NoSuchElementException("넌 삭제할 수 없어!");

        }

        return messageData.remove(messageId);
    }

}

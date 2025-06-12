package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id; // 이 id는 메세지의 id이다
    private final UUID channelId; // 메세지가 있는 채널
    private String content; // 메세지 내용
    
    private final UUID messageId; // 이건 작성자의 id이다
    private final Long createdAt;
    private Long updatedAt;

    // 생성자
    // 메세지를 입력받으면 메세지를 저장하고, 입력된 날짜를 생성한다

    // 누가, 어느 채널에서, 어떤 내용을 썼는지 입력받는다. setter
    public Message(UUID messageId, UUID channelId, String content) { // public으로 만들어야한다
        this.messageId = messageId;
        this.channelId = channelId;
        this.content = content;

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    // getter 함수 정의

    public UUID getMessageId() {
        return messageId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }


    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }


    // update 메서드 작성하기(메세지 수정), setter임
    public void updateContent(String newContent) {
            this.content = newContent;
            this.updatedAt = System.currentTimeMillis();
    }
    
    public String toCSV() {
        return id + "," + channelId + "," + content + "," + createdAt + "," + updatedAt;
    }

}

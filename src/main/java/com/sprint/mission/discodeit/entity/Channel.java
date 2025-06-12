package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id; // 채널 uuid
    private String channelName; // 채널의 이름
    private final Long createdAt;
    private Long updatedAt;
    // 메뉴 안에서도 메세지를 입력할 수 있어야한다

    public Channel(UUID channelId, String channelName) {
        this.channelName = channelName;
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateChannel(String newChannelName) {
            channelName = newChannelName;
            updatedAt = System.currentTimeMillis();
    }

    public String toCSV() {
        return id + "," + channelName + "," + createdAt + "," + updatedAt;
    }
    
}

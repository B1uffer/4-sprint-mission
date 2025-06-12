package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface channelService {

    // channel에 대한 CRUD
    public Channel createChannel(UUID channelId, String channelName);

    public Channel readChannel(UUID channelId);

    public Channel updateChannel(UUID channelId, String newChannelName);

    public Channel deleteChannel(UUID channelId);

}

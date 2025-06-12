package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.channelService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFChannel implements channelService {

    private final Map<UUID, Channel> channelList;
    // 채널 리스트를 만들 때 <Key, Value>로 만드는데 타입설정을 잘해줘야한다.

    public JCFChannel() {

        this.channelList = new HashMap<>();

    }


    public Channel createChannel(UUID channelId, String channelName) {

        Channel channel = new Channel(channelId, channelName);

        this.channelList.put(channelId, channel);

        return channel;

    }

    public Channel readChannel(UUID channelId) {

        if(channelList.containsKey(channelId)) {

            Channel findChannel = this.channelList.get(channelId);

            return findChannel;

        }

        return null;

    }

    public  Channel updateChannel(UUID channelId, String newChannelName) {
        Channel updatedChannel = channelList.get(channelId);

        if(newChannelName != null && !newChannelName.equals(updatedChannel.getChannelName())) {
            updatedChannel.updateChannel(newChannelName);
        }

        return updatedChannel;
    }

    public Channel deleteChannel(UUID channelId) {

        if(!channelList.containsKey(channelId)) {

            throw new NoSuchElementException("넌 채널을 삭제할 수 없어!");

        }

        return channelList.remove(channelId);

    }


}

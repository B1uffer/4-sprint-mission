package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.basic.ChannelException;

public class ChannelNotFoundException extends ChannelException {
    public ChannelNotFoundException(ErrorCode message) {
        super(message);
    }
}

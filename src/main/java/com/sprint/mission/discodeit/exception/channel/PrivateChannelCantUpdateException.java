package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.basic.ChannelException;

public class PrivateChannelCantUpdateException extends ChannelException {
    public PrivateChannelCantUpdateException(ErrorCode message) {
        super(message);
    }
}

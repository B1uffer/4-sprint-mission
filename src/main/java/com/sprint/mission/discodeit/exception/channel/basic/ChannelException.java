package com.sprint.mission.discodeit.exception.channel.basic;

import com.sprint.mission.discodeit.exception.basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;

public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode message) {
        super(message);
    }
}

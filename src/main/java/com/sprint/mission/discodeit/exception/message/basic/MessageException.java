package com.sprint.mission.discodeit.exception.message.basic;

import com.sprint.mission.discodeit.exception.basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode message) {
        super(message);
    }
}

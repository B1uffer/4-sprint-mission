package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.message.basic.MessageException;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(ErrorCode message) {
        super(message);
    }
}

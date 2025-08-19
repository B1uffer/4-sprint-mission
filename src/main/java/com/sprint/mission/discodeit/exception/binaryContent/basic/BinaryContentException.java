package com.sprint.mission.discodeit.exception.binaryContent.basic;

import com.sprint.mission.discodeit.exception.basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;

public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode message) {
        super(message);
    }
}

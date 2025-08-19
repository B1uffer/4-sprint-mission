package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.basic.BinaryContentException;

public class BinaryContentNotFoundException extends BinaryContentException {
    public BinaryContentNotFoundException(ErrorCode message) {
        super(message);
    }
}

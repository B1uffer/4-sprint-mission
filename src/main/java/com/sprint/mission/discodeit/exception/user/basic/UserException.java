package com.sprint.mission.discodeit.exception.user.basic;

import com.sprint.mission.discodeit.exception.basic.DiscodeitException;
import com.sprint.mission.discodeit.exception.basic.ErrorCode;

public class UserException extends DiscodeitException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}

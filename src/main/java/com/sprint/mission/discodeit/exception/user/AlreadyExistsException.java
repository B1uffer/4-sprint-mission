package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.user.basic.UserException;

public class AlreadyExistsException extends UserException {
    public AlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}

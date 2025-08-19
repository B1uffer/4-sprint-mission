package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.basic.ErrorCode;
import com.sprint.mission.discodeit.exception.user.basic.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}

package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

@Getter
public class RoleUpdatedEvent {
    private final Role role;
    private final User user;
    public RoleUpdatedEvent(Role role, User user) {
        this.role = role;
        this.user = user;
    }
}

package com.sprint.mission.discodeit.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RoleUpdatedEventListener {
    @EventListener
    public void handleRoleUpdateEvent(RoleUpdatedEvent event) {
        System.out.println("RoleUpdatedEventListener.handleRoleUpdateEvent");
    }
}

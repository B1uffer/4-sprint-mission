package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;

@Getter
public class BinaryContentUpdatedEvent {
    // BinaryContent 메타 정보가 DB에 잘 저장되었다는 사실을 의미하는 이벤트
    // UserService update
    private final BinaryContent binaryContent;
    private final byte[] binaryContentBytes;
    public BinaryContentUpdatedEvent(BinaryContent binaryContent, byte[] binaryContentBytes) {
        this.binaryContent = binaryContent;
        this.binaryContentBytes = binaryContentBytes;
    }
}

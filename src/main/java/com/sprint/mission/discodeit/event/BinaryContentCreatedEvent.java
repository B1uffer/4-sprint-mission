package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Getter;

import java.util.Optional;

@Getter
public class BinaryContentCreatedEvent {
    // BinaryContent 메타 정보가 DB에 잘 저장되었다는 사실을 의미하는 이벤트
    // UserService create, MessageService create, BinaryContentService create
    private final BinaryContent binaryContent;
    private final byte[] binaryContentBytes;

    public BinaryContentCreatedEvent(BinaryContent binaryContent, byte[] binaryContentBytes) {
        this.binaryContent = binaryContent;
        this.binaryContentBytes = binaryContentBytes;
    }
}

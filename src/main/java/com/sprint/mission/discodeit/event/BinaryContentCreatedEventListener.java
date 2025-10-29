package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;
import java.util.UUID;

@Component
public class BinaryContentCreatedEventListener {
    // 이벤트를 발행한 메인 서비스의 트랜잭션이 커밋되었을 때 리스너가 실행되도록 설정하기
    private final BinaryContentStorage binaryContentStorage;
    private final BasicBinaryContentService basicBinaryContentService;

    public BinaryContentCreatedEventListener(BinaryContentStorage binaryContentStorage, BasicBinaryContentService basicBinaryContentService) {
        this.binaryContentStorage = binaryContentStorage;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    // 트랜잭션이 commit되었을 때 실행
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener(condition = "#event.binaryContent.status == 'PROCESSING'")
    public void handleBinaryContentCreatedEvent(BinaryContentCreatedEvent event) {
        System.out.println("★BinaryContentCreatedEvent received★ : " + event.getBinaryContent());

        try {
            UUID binaryContentId = event.getBinaryContent().getId();
            byte[] bytes = event.getBinaryContentBytes();
            binaryContentStorage.put(binaryContentId, bytes);
            basicBinaryContentService.updateStatus(event.getBinaryContent().getId(), BinaryContentStatus.SUCCESS);
        } catch (BinaryContentException e) {
            e.printStackTrace();
            basicBinaryContentService.updateStatus(event.getBinaryContent().getId(), BinaryContentStatus.FAIL);
        }
        System.out.println("★BinaryContentCreatedEvent completed★ : " + event.getBinaryContent());
    }
}

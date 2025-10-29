package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentException;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class BinaryContentUpdatedEventListener {
    // 이벤트를 발행한 메인 서비스의 트랜잭션이 커밋되었을 때 리스너가 실행되도록 설정하기
    // Listener에서 구현해야함
    private final BinaryContentStorage binaryContentStorage;
    private final BasicBinaryContentService basicBinaryContentService;

    public BinaryContentUpdatedEventListener(BinaryContentStorage binaryContentStorage, BasicBinaryContentService basicBinaryContentService) {
        this.binaryContentStorage = binaryContentStorage;
        this.basicBinaryContentService = basicBinaryContentService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // commit 이후 실행
    @EventListener
    public void handleBinaryContentUpdatedEvent(BinaryContentUpdatedEvent event) {
        System.out.println("☆BinaryContentUpdatedEvent received☆ : " + event.getBinaryContent());

        try {
            UUID binaryContentId = event.getBinaryContent().getId();
            byte[] bytes = event.getBinaryContentBytes();
            binaryContentStorage.put(binaryContentId, bytes);
            basicBinaryContentService.updateStatus(event.getBinaryContent().getId(), BinaryContentStatus.SUCCESS);
        } catch(BinaryContentException e) {
            e.printStackTrace();
            basicBinaryContentService.updateStatus(event.getBinaryContent().getId(), BinaryContentStatus.FAIL);
        }
        System.out.println("☆BinaryContentUpdateEvent put complete☆ : " + event.getBinaryContent());
    }
}

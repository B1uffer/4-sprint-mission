package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class BinaryContentUpdatedEventListener {
    // 이벤트를 발행한 메인 서비스의 트랜잭션이 커밋되었을 때 리스너가 실행되도록 설정하기
    // Listener에서 구현해야함
    private final BinaryContentStorage binaryContentStorage;
    public BinaryContentUpdatedEventListener(BinaryContentStorage binaryContentStorage) {
        this.binaryContentStorage = binaryContentStorage;
    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // commit 이후 실행
    @EventListener
    public void handleBinaryContentUpdatedEvent(BinaryContentUpdatedEvent event) {
        System.out.println("☆BinaryContentUpdatedEvent received☆ : " + event.getBinaryContent());

        UUID binaryContentId = event.getBinaryContent().getId();
        byte[] bytes = event.getBinaryContentBytes();
        binaryContentStorage.put(binaryContentId, bytes);

        System.out.println("☆BinaryContentUpdateEvent put complete☆ : " + event.getBinaryContent());
    }
}

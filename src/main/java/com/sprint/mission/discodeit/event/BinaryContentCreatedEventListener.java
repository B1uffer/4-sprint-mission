package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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

    public BinaryContentCreatedEventListener(BinaryContentStorage binaryContentStorage) {
        this.binaryContentStorage = binaryContentStorage;
    }

    // 트랜잭션이 commit되었을 때 실행
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener(condition = "#event.binaryContent.status == 'PROCESSING'")
    public void handleBinaryContentCreatedEvent(BinaryContentCreatedEvent event) {
        System.out.println("★BinaryContentCreatedEvent received★ : " + event.getBinaryContent());

        UUID binaryContentId = event.getBinaryContent().getId();
        byte[] bytes = event.getBinaryContentBytes();
        binaryContentStorage.put(binaryContentId, bytes);

        System.out.println("★BinaryContentCreatedEvent completed★ : " + event.getBinaryContent());
    }
}

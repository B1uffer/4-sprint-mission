package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor // ?
@AllArgsConstructor
public class Notification extends BaseUpdatableEntity {
    // id, createdAt, updatedAt 상속받음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", columnDefinition = "uuid")
    private User receiverId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;
}

package com.sprint.mission.discodeit.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String refreshToken;

    private UUID userId;

    private LocalDateTime expiredAt;

    private Boolean rotated;

    public void invalidate() {
        this.expiredAt = LocalDateTime.now();
        this.rotated = true;
    }
}

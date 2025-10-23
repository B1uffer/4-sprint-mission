package com.sprint.mission.discodeit.auth.dto;

import lombok.Getter;

@Getter
public class TokenReissueRequest {
    private String accessToken;
    private String refreshToken;
}

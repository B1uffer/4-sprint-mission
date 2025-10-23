package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.JwtDto;
import com.sprint.mission.discodeit.auth.dto.TokenReissueRequest;
import com.sprint.mission.discodeit.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> reissue(@RequestBody TokenReissueRequest request) {
        if(request.getRefreshToken().isEmpty()) {
            return ResponseEntity.badRequest().body(new JwtDto());
        } else {
            return ResponseEntity.ok(tokenService.reissueToken(request.getRefreshToken()));
        }
    }
}

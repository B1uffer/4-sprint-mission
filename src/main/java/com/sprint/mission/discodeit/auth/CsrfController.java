package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/auth")
public class CsrfController {
//    private final DiscodeitUserDetailsService userDetailsService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken(); // 명시적으로 메서드에서 토큰을 호출함
        log.debug("CSRF 토큰 요청 : {}", tokenValue);

        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION) // 이게 203, body 없음(void)
                .header("X-CSRF-TOKEN", tokenValue)
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUserMe(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUserDto());
    }

    // 사용자 권한을 수정하는 API 구현
    @PreAuthorize("hasRole('ADMIN')") // 사용자 권한 수정은 ADMIN 권한을 가져야함
    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
        return ResponseEntity.ok().body(userDetails.getUserDto()); // 200 UserDto, 이게 어떻게 되는건지 공부해야함
    }

}

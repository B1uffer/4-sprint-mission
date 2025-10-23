package com.sprint.mission.discodeit.auth.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        // 쿠키에 저장된 리프레시 토큰을 삭제, onAuthenticationSuccess에서 쿠키를 생성하고 TokenService에서 재발급함
        response.addCookie(deleteCookie(request));
    }

    private Cookie deleteCookie(HttpServletRequest request) {
        Cookie cookie = new Cookie("RereshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 삭제하는거니까 유효기간을 0으로
        return cookie;
    }
}

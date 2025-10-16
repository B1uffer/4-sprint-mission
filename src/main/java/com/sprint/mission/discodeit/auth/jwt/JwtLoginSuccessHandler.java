package com.sprint.mission.discodeit.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.auth.dto.JwtDto;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String accessToken = delegateAccessToken(user);
        String refreshToken = delegateRefreshToken(user);

        // refreshToken을 쿠키에 담기
        Cookie cookie = new Cookie("RefreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(123123); // 유효기간

        // 쿠키를 만들어서 응답에 담기
        response.addCookie(cookie);

        /**
         * BinaryContentDto부터 UserDto 만드는 과정까지는 유저쪽으로 넘기는게 맞다, 리팩토링을 해야함
         */
        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfile());

        // id, username, email, profileDto, online, role
        UserDto userDto = new UserDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentDto,
                user.getStatus().isOnline(),
                user.getRole());

        // 200 JwtDto로 응답하기
        response.setStatus(HttpServletResponse.SC_OK); // 이게 200
        JwtDto jwtDto = new JwtDto(userDto, accessToken); // userDto 만들어야함, accessToken 들어있음
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtDto)); // jwtDto
    }

    private String delegateAccessToken(User user) { // jwtTokenprovider를 이용해 AccessToken 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getEmail());
        claims.put("roles", user.getRole());

        String subject = user.getEmail();

        String accessToken = jwtTokenProvider.generateAccessToken(claims, subject); // JwtTokenProvider를 활용해서 토큰 생성
        return accessToken;
    }

    private String delegateRefreshToken(User user) { // RefreshToken
        String subject = user.getEmail();

        String refreshToken = jwtTokenProvider.generateRefreshToken(subject);
        return refreshToken;
    }
}

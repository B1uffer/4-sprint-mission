package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.JwtDto;
import com.sprint.mission.discodeit.auth.entity.RefreshToken;
import com.sprint.mission.discodeit.auth.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.auth.repository.RefreshTokenRepository;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final BinaryContentMapper binaryContentMapper;

    public JwtDto reissueToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token has expired");
        }

        if(refreshToken.getRotated()) {
            throw new IllegalArgumentException("Refresh token has rotated");
        }

        // 검증 끝
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", refreshToken.getUserId());

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfile());

        UserDto userDto = new UserDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                binaryContentDto,
                user.getStatus().isOnline(),
                user.getRole());

        String subject = user.getEmail();

        String newAccessToken = jwtTokenProvider.generateAccessToken(claims, subject); // accessToken

        // Rotaion을 통해 보안 강화
        boolean shoudRotate = refreshToken.getExpiredAt().isBefore(LocalDateTime.now().plusDays(3));
        if(shoudRotate) {
            refreshToken.invalidate();
        }

        String newRefreshToken = jwtTokenProvider.generateRefreshToken(subject); // refreshToken

        Cookie cookie = new Cookie("RefreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(123123);

        return new JwtDto(userDto, newAccessToken);
    }
}

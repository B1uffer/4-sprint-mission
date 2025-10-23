package com.sprint.mission.discodeit.auth.filter;

import com.sprint.mission.discodeit.auth.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 요청당 한번만 실행되도록 OncePerRequestFilter 상속
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) { // Bearer 이 포함된 토큰만 인증함
            throw new ServletException("올바른 토큰 형식이 아닙니다.");
        } else {
            String jws = request.getHeader("Authorization").replace("Bearer ", "");
            Map<String, Object> claims = jwtTokenProvider.getClaims(jws); // JwtProvider를 통해 엑세스 토큰 유효성 검사
            UserDetails details = userDetailsService.loadUserByUsername(claims.get("username").toString()); // 밑에 details

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    details,
                    null,
                    details.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}

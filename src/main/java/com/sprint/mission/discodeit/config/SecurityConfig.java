package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.auth.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.jwt.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.auth.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.security.Http403ForbiddenAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.*;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

// Method Security 활성화
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, LoginFailureHandler loginFailureHandler, JwtTokenProvider jwtTokenProvider, JwtLoginSuccessHandler jwtLoginSuccessHandler) throws Exception {
        http
                .csrf(csrf -> csrf // 토큰발급
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                )
                .formLogin(login -> login // 로그인
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(jwtLoginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                )
                .logout(logout -> logout // 로그아웃
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)) // 204

                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                        .requestMatchers("/api/auth/csrf-token").permitAll() // Csrf Token 발급
                        .requestMatchers("/api/users").permitAll() // 회원가입
                        .requestMatchers("/api/auth/login").permitAll() // 로그인
                        .requestMatchers("/api/auth/logout").permitAll() // 로그아웃
                        .requestMatchers("/**").permitAll() // API가 아닌 요청
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/manager").hasRole("CHANNEL_MANAGER")
                        .requestMatchers("/user").hasRole("USER"))
                .exceptionHandling(ex -> ex // 적절한 권한이 없는 경우 403 응답을 반환
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                        .accessDeniedHandler(new Http403ForbiddenAccessDeniedHandler())
                )
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 정책을 STATELESS로 변경함, SessionConcurrency 설정 삭제
                )
                .rememberMe(Customizer.withDefaults()); // rememberMe
        return http.build();
    }

    // 계층 권한 설정하기, 계층 구조 정의하기
    // https://juintination.tistory.com/entry/Spring-Security-RoleHierarchy-%EA%B3%84%EC%B8%B5%EA%B6%8C%ED%95%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ADMIN > CHANNEL_MANAGER\n" + "CHANNEL_MANAGER > USER");
        return hierarchy;
    }

    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}

// https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa
// Single-page Applications 참고
final class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler {
    private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
    private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
        this.xor.handle(request, response, csrfToken);
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        String headerValue = request.getHeader(csrfToken.getHeaderName());
        return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request,csrfToken);
    }
}

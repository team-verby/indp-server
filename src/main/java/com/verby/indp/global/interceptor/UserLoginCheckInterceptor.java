package com.verby.indp.global.interceptor;

import com.verby.indp.domain.auth.User;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.auth.service.AuthTokenService;
import com.verby.indp.domain.common.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class UserLoginCheckInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final AuthTokenService authTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            Long userId = authTokenService.decodeUserToken(
                authorization.substring(BEARER_PREFIX.length()));

            User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("권한이 없습니다."));

            request.setAttribute("user", user);
            return true;
        }

        throw new AuthException("로그인이 필요한 서비스입니다.");
    }
}

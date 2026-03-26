package com.verby.indp.global.interceptor;

import com.verby.indp.domain.auth.Admin;
import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.common.exception.AuthException;
import com.verby.indp.global.jwt.TokenManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class AdminLoginCheckInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AdminRepository adminRepository;
    private final TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            Long adminId = tokenManager.decodeAdminToken(authorization.substring(BEARER_PREFIX.length()));

            Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AuthException("권한이 없습니다."));

            request.setAttribute("admin", admin);
            return true;
        }

        throw new AuthException("로그인이 필요한 서비스입니다.");
    }
}

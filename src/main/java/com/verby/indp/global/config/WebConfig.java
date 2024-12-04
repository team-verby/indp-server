package com.verby.indp.global.config;

import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.global.interceptor.LoginCheckInterceptor;
import com.verby.indp.global.jwt.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TokenManager tokenManager;
    private final AdminRepository adminRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor(adminRepository, tokenManager))
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/login");
    }
}

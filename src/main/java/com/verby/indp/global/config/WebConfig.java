package com.verby.indp.global.config;

import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.service.AuthTokenService;
import com.verby.indp.global.interceptor.AdminLoginCheckInterceptor;
import com.verby.indp.global.interceptor.OwnerLoginCheckInterceptor;
import com.verby.indp.global.resolver.LoginAdminArgumentResolver;
import com.verby.indp.global.resolver.LoginOwnerArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthTokenService authTokenService;
    private final AdminRepository adminRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginOwnerArgumentResolver());
        resolvers.add(new LoginAdminArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginCheckInterceptor(adminRepository, authTokenService))
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/login");

        registry.addInterceptor(new OwnerLoginCheckInterceptor(ownerRepository, authTokenService))
            .addPathPatterns("/api/owner/**")
            .excludePathPatterns("/api/owner/login");
    }
}

package com.verby.indp.global.config;

import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.global.interceptor.AdminLoginCheckInterceptor;
import com.verby.indp.global.interceptor.OwnerLoginCheckInterceptor;
import com.verby.indp.global.jwt.TokenManager;
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

    private final TokenManager tokenManager;
    private final AdminRepository adminRepository;
    private final OwnerRepository ownerRepository;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginOwnerArgumentResolver());
        resolvers.add(new LoginAdminArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginCheckInterceptor(adminRepository, tokenManager))
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/login");

        registry.addInterceptor(new OwnerLoginCheckInterceptor(ownerRepository, tokenManager))
            .addPathPatterns("/api/owner/**")
            .excludePathPatterns("/api/owner/login");
    }
}

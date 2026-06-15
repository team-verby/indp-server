package com.verby.indp.global.config;

import com.verby.indp.domain.auth.repository.AdminRepository;
import com.verby.indp.domain.auth.repository.OwnerRepository;
import com.verby.indp.domain.auth.repository.UserRepository;
import com.verby.indp.domain.auth.service.AuthTokenService;
import com.verby.indp.domain.creator.repository.CreatorRepository;
import com.verby.indp.global.interceptor.AdminLoginCheckInterceptor;
import com.verby.indp.global.interceptor.CreatorLoginCheckInterceptor;
import com.verby.indp.global.interceptor.OwnerLoginCheckInterceptor;
import com.verby.indp.global.interceptor.UserLoginCheckInterceptor;
import com.verby.indp.global.resolver.LoginAdminArgumentResolver;
import com.verby.indp.global.resolver.LoginCreatorArgumentResolver;
import com.verby.indp.global.resolver.LoginOwnerArgumentResolver;
import com.verby.indp.global.resolver.LoginUserArgumentResolver;
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
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginOwnerArgumentResolver());
        resolvers.add(new LoginAdminArgumentResolver());
        resolvers.add(new LoginCreatorArgumentResolver());
        resolvers.add(new LoginUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginCheckInterceptor(adminRepository, authTokenService))
            .addPathPatterns("/api/admin/**")
            .excludePathPatterns("/api/admin/login");

        registry.addInterceptor(new OwnerLoginCheckInterceptor(ownerRepository, authTokenService))
            .addPathPatterns("/api/owner/**")
            .excludePathPatterns("/api/owner/login");

        registry.addInterceptor(new CreatorLoginCheckInterceptor(creatorRepository, authTokenService))
            .addPathPatterns("/api/dj/**")
            .excludePathPatterns("/api/dj/playlists", "/api/dj/playlists/**");

        registry.addInterceptor(new UserLoginCheckInterceptor(userRepository, authTokenService))
            .addPathPatterns("/api/user/subscription", "/api/user/payments");
    }
}

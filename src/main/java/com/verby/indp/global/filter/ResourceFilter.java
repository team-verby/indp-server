package com.verby.indp.global.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ResourceFilter implements Filter {

    private static final List<String> ALLOWED_PATHS = Arrays.asList(
        "/api/music",
        "/api/stores",
        "/api/main",
        "/api/contacts",
        "/api/admin",
        "/api/regions",
        "/docs/index.html",
        "/profile",
        "/actuator/health"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        boolean isAllowed = ALLOWED_PATHS.stream().anyMatch(requestURI::startsWith);

        if (isAllowed) {
            chain.doFilter(request, response);
        } else {
            response.getWriter().write("Access Denied");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}

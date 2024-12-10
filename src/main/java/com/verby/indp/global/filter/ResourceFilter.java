package com.verby.indp.global.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ResourceFilter implements Filter {

    private static final List<String> ALLOWED_PATHS = Arrays.asList(
        "/api/music",
        "/api/stores",
        "/api/main",
        "/api/contacts",
        "/api/admin",
        "/docs/index.html"
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

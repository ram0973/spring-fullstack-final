package dev.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SpaWebFilter extends OncePerRequestFilter {
    /**
     * Forwards any unmapped paths (except those containing a period) to the client {@code index.html}.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {
        // If request URI includes the contextPath, remove it.
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (!path.startsWith("/api") && !path.startsWith("/management") && !path.startsWith("/v3/api-docs") &&
            !path.contains(".") && path.matches("/(.*)")
        ) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}


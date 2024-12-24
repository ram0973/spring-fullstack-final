package dev.common.security;

import jakarta.servlet.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.csrf.CsrfToken;

import java.io.IOException;

@Log4j2
public final class CsrfCookieFilter implements Filter {

    @Override
    public void doFilter(
        ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken();
        filterChain.doFilter(request, response);
    }
}

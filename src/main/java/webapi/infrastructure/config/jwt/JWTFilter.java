package webapi.infrastructure.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JWTFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String COOKIE_HEADER = "Cookie";

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        logger.info("Processing request to: {}, JWT: {}", httpServletRequest.getRequestURI(), jwt);

        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            logger.info("JWT validated successfully");
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("Authentication set for user: {}", authentication.getName());
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.info("JWT is invalid or missing, proceeding without authentication");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            logger.debug("Token found in Authorization header: {}", bearerToken);
            return bearerToken.substring(7);
        }
        String cookieBearerToken = getCookieToken(request);
        if (Objects.nonNull(cookieBearerToken)) {
            logger.debug("Token found in cookie: {}", cookieBearerToken);
            return cookieBearerToken;
        }
        logger.debug("No valid token found");
        return null;
    }

    private String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.debug("No cookies found in request");
            return null;
        }
        return Arrays.stream(cookies)
                .filter(c -> "access_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
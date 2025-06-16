package webapi.infrastructure.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import webapi.infrastructure.config.PoolProperties;
import webapi.infrastructure.config.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "roles";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    private final long refreshTokenValidityInMilliseconds;

    public TokenProvider(PoolProperties poolProperties) {
        byte[] keyBytes;
        String secret = poolProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn(
                    "Warning: the JWT key used is not Base64-encoded. " +
                            "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            secret = poolProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * poolProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * poolProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();
        this.refreshTokenValidityInMilliseconds = 1000 * poolProperties.getSecurity().getAuthentication().getJwt().getRefreshTokenValidityInSeconds();
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .setIssuedAt(new Date())
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> claims, String subject) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .setIssuedAt(new Date())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = ((List<String>)claims.get(AUTHORITIES_KEY))
                .stream()
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new UserDetails((String) claims.get("email"), "", authorities);
        principal.setUserId(Integer.parseInt(claims.get("id").toString()));
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public UserDetails getUserByClaimToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        UserDetails principal = new UserDetails((String) claims.get("email"), "", new ArrayList<>());
        principal.setUserId(Integer.parseInt(claims.get("id").toString()));

        return principal;
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug(INVALID_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.debug(INVALID_JWT_TOKEN);
        } catch (MalformedJwtException e) {
            log.debug(INVALID_JWT_TOKEN);
        } catch (SignatureException e) {
            log.debug(INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("Token validation error {}", e.getMessage());
        }

        return false;
    }

    public boolean validateTokenOfOpenEdx(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
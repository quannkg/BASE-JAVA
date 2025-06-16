package webapi.infrastructure.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "MmMyY2IzNTYtZmQ0YS00MzZmLTljMDUtNzMxMjkwNjY5MjdkoiseSAndeAAWCCnKQEnesx";
    private static final long ACCESS_TOKEN_EXPIRATION = 86400000; // 1 ngày (24h)
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 ngày

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Tạo Access Token có thời hạn 1 ngày
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Tạo Refresh Token có thời hạn 7 ngày
     */
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Trích xuất username từ JWT
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Kiểm tra token hợp lệ (không kiểm tra user)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Kiểm tra token hợp lệ với user cụ thể
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && validateToken(token);
    }

    /**
     * Lấy thời gian hết hạn của token
     */
    public Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);
    }
}

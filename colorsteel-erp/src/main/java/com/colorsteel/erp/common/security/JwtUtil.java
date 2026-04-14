package com.colorsteel.erp.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final JwtProperties props;

    public JwtUtil(JwtProperties props) {
        this.props = props;
    }

    private SecretKey key() {
        String secret = props.getSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException(
                    "jwt.secret is not configured (check application.yml is not profile-scoped only)");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("jwt.secret must be at least 32 bytes");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String subject, Map<String, Object> extraClaims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + props.getExpirationMs());
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key())
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

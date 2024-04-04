package com.auth.bookstoreauth.utils;

import com.auth.bookstoreauth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${token-secret}")
    private String secret;

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

    /**
     * METHOD FOR GENERATE TOKEN
     * @param currentUser
     * @return token
     */
    public String generateToken(User currentUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", currentUser.getUsername());
        claims.put("userId", currentUser.getUserId());
        return doGenerateToken(claims, currentUser.getUsername());
    }

    /**
     * Set duration token
     * @param claims
     * @param subject
     * @return string jwt
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
          .setSubject(subject)
          .setIssuedAt(new Date())
          .setClaims(claims)
          .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
          .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
          .compact();
    }
}


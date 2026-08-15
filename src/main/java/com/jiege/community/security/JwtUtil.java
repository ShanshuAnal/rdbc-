package com.jiege.community.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Author: 19599
 * @Date: 2026/8/15 19:22
 * @Description:
 */
@Component
public class JwtUtil {
    /**
     * 私钥
     */
    private final SecretKey secretKey;
    /**
     * 过期时间
     */
    private final long expireMillis;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expire-hours}") long expireHours) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = expireHours * 60 * 60 * 1000;
    }

    /**
     * 生成Token
     *
     * @param userId 用户ID
     * @return Token
     */
    public String generateToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .expiration(new Date(System.currentTimeMillis() + expireMillis))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析Token
     *
     * @param token Token 字符串
     * @return Claims 载荷
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

//package com.jiege.community.userauthor;
//
//import com.jiege.community.security.JwtUtil;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.security.SignatureException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
///**
// * @Author: 19599
// * @Date: 2026/8/15 19:22
// * @Description: JwtUtil 单元测试(纯 new,不依赖 Spring 容器)
// */
//class JwtUtilTest {
//
//    /**
//     * 测试密钥:至少 32 字符
//     */
//    private static final String SECRET = "test-secret-key-for-jwt-unit-test-123456";
//
//    private JwtUtil jwtUtil;
//
//    @BeforeEach
//    void setUp() {
//        jwtUtil = new JwtUtil(SECRET, 24);
//    }
//
//    @Test
//    @DisplayName("签发后解析:subject 与原值一致")
//    void generateAndParse() {
//        String token = jwtUtil.generateToken("user-001");
//
//        Claims claims = jwtUtil.parseToken(token);
//
//        assertEquals("user-001", claims.getSubject());
//    }
//
//    @Test
//    @DisplayName("篡改签名:抛出 SignatureException")
//    void parseTamperedToken() {
//        String token = jwtUtil.generateToken("user-001");
//
//        // 修改签名段的最后一个字符(保证仍是合法 base64url 字符)
//        String lastChar = token.substring(token.length() - 1);
//        String tampered = token.substring(0, token.length() - 1)
//                + ("a".equals(lastChar) ? "b" : "a");
//
//        assertThrows(SignatureException.class, () -> jwtUtil.parseToken(tampered));
//    }
//
//    @Test
//    @DisplayName("已过期 token:抛出 ExpiredJwtException")
//    void parseExpiredToken() {
//        // 0 小时 = 立即过期
//        JwtUtil expiredJwtUtil = new JwtUtil(SECRET, 0);
//
//        String token = expiredJwtUtil.generateToken("user-001");
//
//        assertThrows(ExpiredJwtException.class, () -> expiredJwtUtil.parseToken(token));
//    }
//}

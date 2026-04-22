package com.rbac.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Re-zero
 * @version 1.0
 * JWT 令牌生成与解析工具类
 */
@Slf4j
@Component
public class JwtTokenProvider {

    // 密钥 (注意：jjwt 0.11.x 要求 HMAC-SHA256 算法的密钥长度必须 >= 256 bit，即至少 32 个英文字符)
    private final String SECRET = "RbacSystemSuperSecretKeyWithMoreThan32Characters";

    // 过期时间：默认 24 小时 (毫秒)
    private final long EXPIRATION = 86400000L;

    /**
     * 生成安全的签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 根据用户名生成 Token
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(username)       // 将用户名存入载荷 (Payload)
                .setIssuedAt(now)           // 签发时间
                .setExpiration(expiryDate)  // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 签名
                .compact();
    }

    /**
     * 从 Token 中提取用户名
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 校验 Token 是否合法
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("无效的 JWT 签名");
        } catch (ExpiredJwtException e) {
            log.warn("过期的 JWT token");
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的 JWT token");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims 为空");
        }
        return false;
    }
}
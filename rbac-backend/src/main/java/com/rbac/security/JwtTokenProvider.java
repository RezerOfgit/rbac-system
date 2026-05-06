package com.rbac.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 令牌工具类，负责 Token 的生成、解析与校验。
 * @author Re-zero
 * @version 1.0
 */
@Slf4j
@Component
public class JwtTokenProvider {

    // jjwt 0.11.x 要求 HMAC-SHA256 密钥长度 >= 256 bit
    @Value("${jwt.secret}")
    private String secret;

    /** 过期时间（毫秒），当前为 24 小时 */
    private final long EXPIRATION = 86400000L;

    /** 生成安全的签名密钥 */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /** 根据用户名签发 Token */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** 从 Token 中解析用户名 */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /** 校验 Token 合法性，无效时返回 false */
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
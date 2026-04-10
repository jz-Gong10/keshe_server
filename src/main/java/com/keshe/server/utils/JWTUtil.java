package com.keshe.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.refresh-secret-key:${security.jwt.secret-key}}")
    private String refreshSecretKey;

    @Value("${security.jwt.expiration-time:1800000}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time:7200000}")
    private long refreshExpiration;

    public static final int EXPIRE_TIME = 1800;//Token过期时间（秒）
    public static final int REFRESH_EXPIRE_TIME = 2 * 60 * 60;//RefreshToken过期时间（秒）
    
    // 为了兼容LoginService中的静态调用，添加静态变量
    public static String SECRET_KEY = "SGVsbG8gV29ybGQga2VzaGUtc2VydmVyIGp3dC1zZWNyZXQta2V5";
    public static String REFRESH_SECRET_KEY = "SGVsbG8gV29ybGQga2VzaGUtc2VydmVyIHJlZnJlc2gtc2VjcmV0LWtleQ==";
    
    // 初始化静态变量
    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
        REFRESH_SECRET_KEY = refreshSecretKey;
    }

    //生成token
    public String getToken(String accountId, int expireTime, String key) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_id", accountId);

            return Jwts
                    .builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                    .signWith(getSignInKey(key), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("生成Token失败");
        }
    }

    //返回token内容
    public static Claims getTokenInfo(String token, String key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKeyStatic(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 获取用户 ID
    public String getUserId(String token, String key) {
        try {
            // 验证并解析 Token
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey(key))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 提取用户 ID
            return claims.get("user_id", String.class);

        } catch (Exception e) {
            if (e.getMessage().contains("expired")) {
                // Token 过期
                throw new RuntimeException("Token 已过期，请重新登录");
            } else {
                // Token 无效
                throw new RuntimeException("Token 无效，请检查后重试");
            }
        }
    }

    private Key getSignInKey(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Key getSignInKeyStatic(String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 生成访问令牌
    public String generateAccessToken(String accountId) {
        return getToken(accountId, EXPIRE_TIME, secretKey);
    }

    // 生成刷新令牌
    public String generateRefreshToken(String accountId) {
        return getToken(accountId, REFRESH_EXPIRE_TIME, refreshSecretKey);
    }
}

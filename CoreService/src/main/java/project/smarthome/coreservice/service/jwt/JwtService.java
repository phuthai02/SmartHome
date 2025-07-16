package project.smarthome.coreservice.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.service.redis.RedisService;

import java.security.Key;
import java.util.*;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RedisService redisService;

    public String generateAccessToken(String username) {
        String token = generateToken(username, accessTokenExpiration);

        String key = "jwt:access:" + username;
        redisService.set(key, token, accessTokenExpiration / 1000);

        return token;
    }

    public String generateRefreshToken(String username) {
        String refreshToken = generateToken(username, refreshTokenExpiration);

        String key = "jwt:refresh:" + username;
        redisService.set(key, refreshToken, refreshTokenExpiration / 1000);

        return refreshToken;
    }

    public void revokeAccessToken(String username) {
        String key = "jwt:access:" + username;
        redisService.delete(key);
    }

    public void revokeRefreshToken(String username) {
        String key = "jwt:refresh:" + username;
        redisService.delete(key);
    }

    public boolean isAccessTokenValid(String token, String username) {
        String key = "jwt:access:" + username;
        String storedToken = redisService.get(key);
        return token.equals(storedToken) && !isExpired(token);
    }

    public boolean isRefreshTokenValid(String refreshToken, String username) {
        String key = "jwt:refresh:" + username;
        String storedToken = redisService.get(key);
        return refreshToken.equals(storedToken) && !isExpired(refreshToken);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public long getTimeToExpiration(String token) {
        Date expiration = getExpirationFromToken(token);
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    private Date getExpirationFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private String generateToken(String username, long expiration) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
package project.smarthome.coreservice.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.smarthome.common.utils.Constants;
import project.smarthome.coreservice.service.redis.RedisService;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.token.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RedisService redisService;

    @Override
    public String generateAccessToken(String username) {
        String accessToken = generateToken(username, accessTokenExpiration);
        String key = String.format(Constants.Format.ACCESS_TOKEN, username);
        redisService.set(key, accessToken, accessTokenExpiration / 1000);
        return accessToken;
    }

    @Override
    public String generateRefreshToken(String username) {
        String refreshToken = generateToken(username, refreshTokenExpiration);
        String key = String.format(Constants.Format.REFRESH_TOKEN, username);
        redisService.set(key, refreshToken, refreshTokenExpiration / 1000);
        return refreshToken;
    }

    @Override
    public void revokeAccessToken(String username) {
        String key = String.format(Constants.Format.ACCESS_TOKEN, username);
        redisService.delete(key);
    }

    @Override
    public void revokeRefreshToken(String username) {
        String key = String.format(Constants.Format.REFRESH_TOKEN, username);
        redisService.delete(key);
    }

    @Override
    public Boolean isAccessTokenValid(String accessToken, String username) {
        String key = String.format(Constants.Format.ACCESS_TOKEN, username);
        String storedToken = redisService.get(key);
        return accessToken.equals(storedToken) && !isExpired(accessToken);
    }

    @Override
    public Boolean isRefreshTokenValid(String refreshToken, String username) {
        String key = String.format(Constants.Format.REFRESH_TOKEN, username);
        String storedToken = redisService.get(key);
        return refreshToken.equals(storedToken) && !isExpired(refreshToken);
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Long getTimeToExpiration(String token) {
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

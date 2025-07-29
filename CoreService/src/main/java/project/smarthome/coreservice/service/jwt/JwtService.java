package project.smarthome.coreservice.service.jwt;

public interface JwtService {
    String generateAccessToken(String username);
    String generateRefreshToken(String username);
    void revokeAccessToken(String username);
    void revokeRefreshToken(String username);
    Boolean isAccessTokenValid(String accessToken, String username);
    Boolean isRefreshTokenValid(String refreshToken, String username);
    String extractUsername(String token);
    Long getTimeToExpiration(String token);
}
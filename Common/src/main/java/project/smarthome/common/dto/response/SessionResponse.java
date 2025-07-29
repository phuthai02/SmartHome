package project.smarthome.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private String username;
    private String accessToken;
    private String refreshToken;
    private boolean accessTokenExpired;
    private boolean refreshTokenExpired;
    private long accessTokenTtl;
    private long refreshTokenTtl;
}

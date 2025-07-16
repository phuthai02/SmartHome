package project.smarthome.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.smarthome.common.utils.Constants;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String username;
    private List<String> authorities;
    private String tokenType;
    private Long expiresIn;

    public AuthResponse(String accessToken, String refreshToken, String username, List<String> authorities, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.authorities = authorities;
        this.tokenType = Constants.JWT_BEARER;
        this.expiresIn = expiresIn;
    }
}

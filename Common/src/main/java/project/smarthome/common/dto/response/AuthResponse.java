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
    private UserInfo userInfo;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

    public AuthResponse(UserInfo userInfo, String accessToken, String refreshToken, Long expiresIn) {
        this.userInfo = userInfo;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = Constants.JWT_BEARER;
        this.expiresIn = expiresIn;
    }
}

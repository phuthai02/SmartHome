package project.smarthome.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.smarthome.common.utils.Constants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private UserResponse userInfo;
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

    public AuthResponse(UserResponse userInfo, String accessToken, String refreshToken, Long expiresIn) {
        this.userInfo = userInfo;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = Constants.JWT_BEARER;
        this.expiresIn = expiresIn;
    }
}

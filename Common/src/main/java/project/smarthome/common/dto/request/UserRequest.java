package project.smarthome.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String fullName;
    private String avatarBase64;
    private String email;
    private String phoneNumber;
}
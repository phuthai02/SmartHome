package project.smarthome.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.smarthome.common.entity.mysql.Home;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String fullName;
    private String avatarBase64;
    private String avatarText;
    private String role;
    private String email;
    private String phoneNumber;
    private List<Home> homes;
}

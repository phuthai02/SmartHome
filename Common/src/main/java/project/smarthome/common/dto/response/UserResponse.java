package project.smarthome.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.smarthome.common.entity.mysql.Home;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private String avatarBase64;
    private String avatarText;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean status;
    private Boolean online;
    private Timestamp createdTime;
    private List<Home> homes;
}

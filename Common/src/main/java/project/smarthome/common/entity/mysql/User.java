package project.smarthome.common.entity.mysql;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import project.smarthome.common.dto.response.UserResponse;
import project.smarthome.common.utils.Constants;
import project.smarthome.common.utils.Utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Lob
    @Column(name = "AVATAR", columnDefinition = "LONGBLOB")
    private byte[] avatar;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "STATUS")
    private String status;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-home")
    private List<Home> homes = new ArrayList<>();

    public UserResponse getUserInfo() {
        UserResponse info = new UserResponse();
        BeanUtils.copyProperties(this, info);

        info.setAvatarBase64(this.avatar != null ? Base64.getEncoder().encodeToString(this.avatar) : null);
        info.setAvatarText(Utils.getAvatarInitials(this.fullName));

        info.setRole(Constants.Role.ADMIN.equals(this.role) ? Constants.Role.ADMIN_TEXT : Constants.Role.CUSTOMER_TEXT);
        info.setStatus(Constants.Status.ACTIVE.equals(this.status) ? Constants.Status.ACTIVE_TEXT : Constants.Status.LOCKED_TEXT);
        return info;
    }
}

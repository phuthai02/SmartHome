package project.smarthome.common.entity.mysql;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_FCM_TOKEN")
public class UserFcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("user-fcm-token")
    private User user;

    @Column(name = "FCM_TOKEN")
    private String fcmToken;
}


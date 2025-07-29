package project.smarthome.common.entity.mongodb;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = DeviceCommandHistory.COLLECTION_NAME)
public class DeviceCommandHistory {
    public static final String COLLECTION_NAME = "device_command_history";
    @Id
    private String id;
    private Long deviceId;
    private String request;
    private String response;
    private String status;
    private Long createdUser;
    private LocalDateTime createdTime;
}

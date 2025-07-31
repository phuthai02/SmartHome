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
@Document(collection = AlertHistory.COLLECTION_NAME)
public class AlertHistory {
    public static final String COLLECTION_NAME = "alert_history";
    @Id
    private String id;
    private Long deviceId;
    private String alertMessage;
    private String severity; // Ví dụ: LOW, MEDIUM, HIGH
    private LocalDateTime timestamp;
}

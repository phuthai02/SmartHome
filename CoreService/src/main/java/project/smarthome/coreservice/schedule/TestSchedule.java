package project.smarthome.coreservice.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.smarthome.coreservice.service.fcm.FCMService;
import project.smarthome.coreservice.service.mqtt.MqttService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TestSchedule {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private MqttService mqttService;

    private static final String DEVICE_TOKEN = "fn9Egl4kT3u_GCj1d0oggQ:APA91bEcYLPzmcunTv5hQVC33eKzbObsXTgOf4M0_bznRxNd-EcoD_bw-t-k251tMhUuTsJMQUG_dk7rAJQu3aYUoH8VfKc3FIn3FabJD04S3TBCmsADBKY";

    private static final String TEST_TOPIC = "SMARTHOME/ALERT";

    @PostConstruct
    public void initSubscribe() {
        mqttService.subscribe(TEST_TOPIC, (topic, payload) -> {
            String title = "Cảnh báo khẩn cấp";
            String body = payload;

            // Gửi FCM
            try {
                String response = fcmService.sendNotification(title, body, DEVICE_TOKEN);
                System.out.println("[NOTI JOB] Sent notification: " + response);
            } catch (Exception e) {
                System.err.println("[NOTI JOB] Error sending notification: " + e.getMessage());
            }
        });
    }

//    @Scheduled(fixedRate = 10000)
    public void sendNotificationJob() {
        // Publish MQTT
        String mqttMessage = "Công ty cháy rồi :))";
        mqttService.publish(TEST_TOPIC, mqttMessage);
    }
}

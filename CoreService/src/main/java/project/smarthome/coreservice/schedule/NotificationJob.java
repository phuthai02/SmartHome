package project.smarthome.coreservice.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.smarthome.coreservice.service.fcm.FCMService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NotificationJob {

    @Autowired
    private FCMService fcmService;

    private static final String DEVICE_TOKEN = "fn9Egl4kT3u_GCj1d0oggQ:APA91bEcYLPzmcunTv5hQVC33eKzbObsXTgOf4M0_bznRxNd-EcoD_bw-t-k251tMhUuTsJMQUG_dk7rAJQu3aYUoH8VfKc3FIn3FabJD04S3TBCmsADBKY";


    @Scheduled(fixedRate = 10000)
    public void sendNotificationJob() {
        String title = "SmartHome Notification";
        String body = "Hello! Time now: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try {
            String response = fcmService.sendNotification(title, body, DEVICE_TOKEN);
            System.out.println("[NOTI JOB] Sent notification: " + response);
        } catch (Exception e) {
            System.err.println("[NOTI JOB] Error sending notification: " + e.getMessage());
        }
    }
}

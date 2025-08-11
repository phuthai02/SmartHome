package project.smarthome.coreservice.service.fcm;

public interface FCMService {
    String sendNotification(String title, String body, String deviceToken) throws Exception;
}


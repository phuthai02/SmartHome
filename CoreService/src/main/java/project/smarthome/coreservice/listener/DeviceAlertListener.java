package project.smarthome.coreservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.smarthome.common.entity.mysql.Device;
import project.smarthome.coreservice.service.device.DeviceService;
import project.smarthome.coreservice.service.fcm.FCMService;
import project.smarthome.coreservice.service.mqtt.MqttService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class DeviceAlertListener {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private MqttService mqttService;

    @Autowired
    private DeviceService deviceService;

    @PostConstruct
    public void init() {
        List<Device> devices = deviceService.getAllDevices();
        for (Device device : devices) {
            mqttService.subscribe(device.getTopicAlert(), (topic, payload) -> {
                if (device.getUser() != null && device.getUser().getFcmTokens() != null && !device.getUser().getFcmTokens().isEmpty()) {
                    device.getUser().getFcmTokens().forEach(fcmToken -> {
                        try {
                            fcmService.sendNotification("Cảnh báo khẩn cấp", payload, fcmToken.getFcmToken());
                        } catch (Exception e) {
                        }
                    });
                }
            });
        }
    }
}

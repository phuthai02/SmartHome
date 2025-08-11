package project.smarthome.coreservice.service.mqtt;

import java.util.function.BiConsumer;

public interface MqttService {
    void publish(String topic, String payload);
    void subscribe(String topic, BiConsumer<String, String> callback);
}
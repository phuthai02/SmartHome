package project.smarthome.coreservice.service.mqtt;

public interface MqttService {
    void publish(String topic, String payload);
}
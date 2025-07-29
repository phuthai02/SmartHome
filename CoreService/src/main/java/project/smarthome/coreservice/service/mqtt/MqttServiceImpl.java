package project.smarthome.coreservice.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttServiceImpl implements MqttService {

    @Autowired
    private MessageChannel mqttOutboundChannel;

    @Override
    public void publish(String topic, String payload) {
        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader("topic", topic)
                .build();
        mqttOutboundChannel.send(message);
        log.info("[MQTT] Message published successfully - {}: {}", topic, payload);
    }
}

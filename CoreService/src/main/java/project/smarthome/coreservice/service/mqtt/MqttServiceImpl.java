package project.smarthome.coreservice.service.mqtt;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class MqttServiceImpl implements MqttService {

    @Autowired
    private MessageChannel mqttOutboundChannel;

    @Autowired
    private Mqtt5AsyncClient mqttAsyncClient;

    @Override
    public void publish(String topic, String payload) {
        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader("topic", topic)
                .build();
        mqttOutboundChannel.send(message);
        log.info("[MQTT] Message published >> [Topic: {}, Payload: {}]", topic, payload);
    }

    @Override
    public void subscribe(String topic, BiConsumer<String, String> callback) {
        mqttAsyncClient.subscribeWith().topicFilter(topic).callback(publish -> {
            String payload = StandardCharsets.UTF_8.decode(publish.getPayload().get()).toString();
            log.info("[MQTT] Message received >> [Topic: {}, Payload: {}]", publish.getTopic(), payload);
            callback.accept(topic, payload);
        }).send().whenComplete((subAck, throwable) -> {
            if (throwable != null) throw new RuntimeException(throwable);
        });
    }
}

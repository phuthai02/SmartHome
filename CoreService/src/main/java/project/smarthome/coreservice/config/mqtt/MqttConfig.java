package project.smarthome.coreservice.config.mqtt;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
public class MqttConfig {

    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.port}")
    private int port;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.default-topic}")
    private String defaultTopic;

    @Bean
    public Mqtt5BlockingClient hiveMqttClient() {
        Mqtt5BlockingClient client = com.hivemq.client.mqtt.MqttClient.builder()
                .useMqttVersion5()
                .identifier(clientId + "-" + UUID.randomUUID().toString().substring(0, 8))
                .serverHost(host)
                .serverPort(port)
                .sslWithDefaultConfig()
                .buildBlocking();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(StandardCharsets.UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        return client;
    }

    @Bean
    public Mqtt5AsyncClient mqttAsyncClient(Mqtt5BlockingClient blockingClient) {
        return blockingClient.toAsync();
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutboundHandler(Mqtt5BlockingClient blockingClient) {
        return message -> {
            String topic = (String) message.getHeaders().getOrDefault("topic", defaultTopic);
            String payload = (String) message.getPayload();
            blockingClient.publishWith().topic(topic).payload(StandardCharsets.UTF_8.encode(payload)).send();
        };
    }
}

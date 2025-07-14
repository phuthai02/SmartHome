package project.smarthome.common.utils;

import java.util.UUID;

public class MqttUtils {

    public static String generateMqttTopic(Integer topicType) {
        String topic;
        String suffix = UUID.randomUUID().toString();
        topic = switch (topicType) {
            case 1 -> String.format(Constants.PREFIX_TOPIC_COMMAND, suffix);
            case 2 -> String.format(Constants.PREFIX_TOPIC_CONTENT, suffix);
            case 3 -> String.format(Constants.PREFIX_TOPIC_ALERT, suffix);
            default -> throw new IllegalArgumentException("Invalid topic type: " + topicType);
        };
        return topic;
    }

}

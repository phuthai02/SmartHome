package project.smarthome.coreservice.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.smarthome.coreservice.service.mqtt.MqttService;

@Component
public class TestSchedule {

    @Autowired
    private MqttService mqttService;

    @Scheduled(fixedRate = 3000)
    public void TestSchedule() {
//        mqttService.publish("smarthome/led", "TEST");
    }
}

package project.smarthome.coreservice.controller.cstm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("device")
public class DeviceTController {
    @GetMapping
    public String cstm() {
        return "cstm";
    }
}

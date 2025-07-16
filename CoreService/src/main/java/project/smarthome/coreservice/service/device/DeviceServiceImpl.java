package project.smarthome.coreservice.service.device;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.coreservice.feignclient.device.DeviceFeignClient;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceFeignClient deviceFeignClient;
}

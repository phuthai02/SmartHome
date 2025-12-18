package project.smarthome.coreservice.service.device;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.Device;
import project.smarthome.coreservice.feignclient.device.DeviceFeignClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceFeignClient deviceFeignClient;

    @Override
    public List<Device> getAllDevices() {
        List<Device> devices = new ArrayList<>();
        try {
            devices = deviceFeignClient.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return devices;
    }
}

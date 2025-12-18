package project.smarthome.coreservice.service.device;

import project.smarthome.common.entity.mysql.Device;

import java.util.List;

public interface DeviceService {
    List<Device> getAllDevices();
}

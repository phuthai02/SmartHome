package project.smarthome.dataservice.service.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.Device;
import project.smarthome.dataservice.repository.DeviceRepository;
import project.smarthome.dataservice.service.BaseService;

@Service
public class DeviceService extends BaseService<Device, Long> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    protected JpaRepository<Device, Long> getRepository() {
        return deviceRepository;
    }
}

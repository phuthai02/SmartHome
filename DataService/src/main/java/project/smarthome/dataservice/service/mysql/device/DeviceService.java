package project.smarthome.dataservice.service.mysql.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.Device;
import project.smarthome.dataservice.repository.mysql.DeviceRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

@Service
public class DeviceService extends BaseMySQLService<Device, Long> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    protected JpaRepository<Device, Long> getRepository() {
        return deviceRepository;
    }

    @Override
    protected JpaSpecificationExecutor<Device> getSpecificationExecutor() {
        return deviceRepository;
    }
}

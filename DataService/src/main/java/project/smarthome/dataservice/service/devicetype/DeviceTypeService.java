package project.smarthome.dataservice.service.devicetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.DeviceType;
import project.smarthome.dataservice.repository.DeviceTypeRepository;
import project.smarthome.dataservice.service.BaseService;

@Service
public class DeviceTypeService extends BaseService<DeviceType, Long> {

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Override
    protected JpaRepository<DeviceType, Long> getRepository() {
        return deviceTypeRepository;
    }
}

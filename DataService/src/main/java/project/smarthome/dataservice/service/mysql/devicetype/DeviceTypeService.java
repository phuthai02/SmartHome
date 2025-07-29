package project.smarthome.dataservice.service.mysql.devicetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import project.smarthome.common.entity.mysql.DeviceType;
import project.smarthome.dataservice.repository.mysql.DeviceTypeRepository;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;

@Service
public class DeviceTypeService extends BaseMySQLService<DeviceType, Long> {

    @Autowired
    private DeviceTypeRepository deviceTypeRepository;

    @Override
    protected JpaRepository<DeviceType, Long> getRepository() {
        return deviceTypeRepository;
    }

    @Override
    protected JpaSpecificationExecutor<DeviceType> getSpecificationExecutor() {
        return deviceTypeRepository;
    }
}

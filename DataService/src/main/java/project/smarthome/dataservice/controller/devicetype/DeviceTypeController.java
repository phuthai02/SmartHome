package project.smarthome.dataservice.controller.devicetype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mysql.DeviceType;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.mysql.BaseMySQLService;
import project.smarthome.dataservice.service.mysql.devicetype.DeviceTypeService;

@RestController
@RequestMapping("device-type")
public class DeviceTypeController extends BaseController<DeviceType, Long> {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Override
    protected BaseMySQLService<DeviceType, Long> getBaseService() {
        return deviceTypeService;
    }

    @Override
    protected String getEntityName() {
        return "DEVICE TYPE";
    }
}

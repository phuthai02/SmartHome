package project.smarthome.dataservice.controller.mysql.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mysql.Device;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.mysql.device.DeviceService;

@RestController
@RequestMapping("device")
public class DeviceController extends BaseController<Device, Long> {

    @Autowired
    private DeviceService deviceService;

    @Override
    protected BaseService<Device, Long> getBaseService() {
        return deviceService;
    }

    @Override
    protected String getEntityName() {
        return "DEVICE";
    }
}

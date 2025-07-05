package project.smarthome.dataservice.controller.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import project.smarthome.common.entity.Device;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.device.DeviceService;

@Controller
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

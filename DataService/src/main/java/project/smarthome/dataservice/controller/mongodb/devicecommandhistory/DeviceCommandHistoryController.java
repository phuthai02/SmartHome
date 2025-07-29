package project.smarthome.dataservice.controller.mongodb.devicecommandhistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mongodb.DeviceCommandHistory;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.mongodb.devicecommandhistory.DeviceCommandHistoryService;

@RestController
@RequestMapping("device-command-history")
public class DeviceCommandHistoryController extends BaseController<DeviceCommandHistory, String> {

    @Autowired
    DeviceCommandHistoryService deviceCommandHistoryService;

    @Override
    protected BaseService<DeviceCommandHistory, String> getBaseService() {
        return deviceCommandHistoryService;
    }

    @Override
    protected String getEntityName() {
        return "DEVICE COMMAND HISTORY";
    }
}

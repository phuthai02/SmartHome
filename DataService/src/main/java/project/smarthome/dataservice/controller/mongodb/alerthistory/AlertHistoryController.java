package project.smarthome.dataservice.controller.mongodb.alerthistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.smarthome.common.entity.mongodb.AlertHistory;
import project.smarthome.dataservice.controller.BaseController;
import project.smarthome.dataservice.service.BaseService;
import project.smarthome.dataservice.service.mongodb.alerthistory.AlertHistoryService;

@RestController
@RequestMapping("alert-history")
public class AlertHistoryController extends BaseController<AlertHistory, String> {

    @Autowired
    private AlertHistoryService alertHistoryService;

    @Override
    protected BaseService<AlertHistory, String> getBaseService() {
        return alertHistoryService;
    }

    @Override
    protected String getEntityName() {
        return "ALERT HISTORY";
    }
}
